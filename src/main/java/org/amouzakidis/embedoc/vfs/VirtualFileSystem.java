package org.amouzakidis.embedoc.vfs;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.*;

public class VirtualFileSystem {
    private static final int HEADER_SIZE = 1024;
    private final String fileName;
    private final int pageSize;
    private final RandomAccessFile raf;
    private final byte[] header;
    private final List<byte[]> blockBitmap;
    private Map<Integer, VirtualFileBlockMapper> vfbmList;

    public VirtualFileSystem( String fileName, int pageSize ) throws IOException {
        this.fileName = fileName;
        this.pageSize = pageSize;
        this.raf = new RandomAccessFile( this.fileName, "rwd" );
        this.header = new byte[ HEADER_SIZE ];
        blockBitmap = new ArrayList<>();
        vfbmList = new HashMap<>();
        if( this.raf.length() == 0 ){
            initVFS();
        } else {
            openVFS();
        }
    }

    private void initVFS() throws IOException {
        //for now we dont do anything with the header
        for (int i = 0; i < header.length; i ++ ){
            header[i] = 0;
        }
        this.raf.write( header );

        byte[] newBitmap = new byte[ pageSize ];
        for (int i = 0; i < newBitmap.length; i ++ ){
            newBitmap[ i ] = 0;
        }
        this.raf.write(newBitmap);
        newBitmap = Arrays.copyOfRange( newBitmap, 8, newBitmap.length + 1 );
        this.blockBitmap.add( newBitmap );
    }
    private void openVFS() throws IOException {
        this.raf.read( this.header );
        long newBlockIndex = HEADER_SIZE;
        while( newBlockIndex != 0 ){
            this.raf.seek( newBlockIndex );
            byte[] blockBitmap = new byte[ pageSize ];
            this.raf.read( blockBitmap );
            ByteBuffer bb = ByteBuffer.wrap( blockBitmap );
            newBlockIndex = bb.getLong(0 );
            blockBitmap = Arrays.copyOfRange( blockBitmap, 8, blockBitmap.length + 1 );
            this.blockBitmap.add( blockBitmap );
        }

        newBlockIndex = HEADER_SIZE + pageSize;

        while( newBlockIndex != 0 ){
            this.raf.seek( newBlockIndex );
            byte[] fileMapper = new byte[ pageSize ];
            this.raf.read(fileMapper);
            ByteBuffer bb = ByteBuffer.wrap( fileMapper );
            long blockIndex = newBlockIndex;
            newBlockIndex = bb.getLong(0 );
            int fileName = bb.getInt(); // we store only the hash code of the file name to reduce space consumption
            VirtualFileBlockMapper vfbm = this.vfbmList.getOrDefault(fileName, new VirtualFileBlockMapper( fileName, blockIndex ) );
            while( bb.remaining() > 8 ){
                vfbm.addBlock( bb.getLong() );
            }
        }
    }

    public void getOrCreateFile( String fileName ){
        int newFileNameHash = fileName.hashCode();
        VirtualFileBlockMapper vfbm = this.vfbmList.get( newFileNameHash );
        if( vfbm != null ) {

        }
    }
}
