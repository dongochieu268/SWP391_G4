package com.edunac.mentora.service;

import com.edunac.mentora.domain.LearningNode;
import com.edunac.mentora.domain.NodeContent;
import com.edunac.mentora.repository.LearningNodeRepository;
import com.edunac.mentora.repository.NodeContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LearningNodeService {

    @Autowired
    private LearningNodeRepository nodeRepository;

    @Autowired
    private NodeContentRepository contentRepository;


    public List<LearningNode> getNodesByPath(Integer learningPathId) {
        return nodeRepository.findByLearningPathIdOrderByNodeOrderAsc(learningPathId);
    }

    @Transactional
    public LearningNode saveNode(LearningNode node) {
        return nodeRepository.save(node);
    }

    @Transactional
    public LearningNode insertNodeBetween(LearningNode newNode, Double prevOrder, Double nextOrder) {
        double targetOrder = (prevOrder + nextOrder) / 2.0;
        newNode.setNodeOrder(targetOrder);
        return nodeRepository.save(newNode);
    }

    @Transactional
    public void deleteNode(Integer id) {
        nodeRepository.deleteById(id);
    }

    // --- LOGIC CHO NODE CONTENT (UC17) ---

    public List<NodeContent> getContentsByNode(Integer nodeId) {
        return contentRepository.findByLearningNodeIdOrderByDisplayOrderAsc(nodeId);
    }

    @Transactional
    public NodeContent saveContentToNode(Integer nodeId, NodeContent content) {
        LearningNode node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node không tồn tại để gắn học liệu!"));

        content.setLearningNode(node);
        return contentRepository.save(content);
    }

    @Transactional
    public void deleteContent(Integer contentId) {
        contentRepository.deleteById(contentId);
    }
}