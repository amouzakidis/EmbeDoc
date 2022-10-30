package org.amouzakidis.embedoc.vfs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VirtualFileBlockMapper {
    private final int fileName;
    private final long filePosition;
    private final List<Long> blockPointers;
    public VirtualFileBlockMapper( int fileName, long filePosition ) {
        this( fileName, filePosition, new ArrayList<>() );
    }

    public VirtualFileBlockMapper( int fileName, long filePosition, List<Long> blockPointers ) {
        this.fileName = fileName;
        this.filePosition = filePosition;
        this.blockPointers = blockPointers;
    }

    public void addBlock( long newBlockIndex ){
        this.blockPointers.add( newBlockIndex );
    }
}
