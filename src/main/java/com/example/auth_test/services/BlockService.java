package com.example.auth_test.services;

import com.example.auth_test.model.statement.Block;
import com.example.auth_test.repos.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.startup.VersionLoggerListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    public Block getBlock(String blockNumber) {
        return blockRepository.findByNumber(blockNumber)
                .orElseThrow(() -> new RuntimeException("Block does not exist"));
    }

    public void saveBlock(Block block) {
        blockRepository.save(block);
    }

    public boolean blockIsPresent(String blockNumber) {
        return blockRepository.findByNumber(blockNumber).isPresent();
    }

}
