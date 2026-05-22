package com.edunac.mentora.service;
import com.edunac.mentora.repository.NodeContentRepository;
import com.edunac.mentora.repository.LearningNodeRepository;
import com.edunac.mentora.domain.NodeContent;
import com.edunac.mentora.domain.LearningNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LearningNodeService {

    private static final BigDecimal NORMALIZE_THRESHOLD = new BigDecimal("0.001");

    @Autowired
    private LearningNodeRepository nodeRepository;

    @Autowired
    private NodeContentRepository contentRepository;

    public List<LearningNode> getNodesByPath(Integer learningPathId) {
        return nodeRepository.findByLearningPathIdOrderByNodeOrderAsc(learningPathId);
    }

    // Trả về order cho node thêm vào cuối
    public BigDecimal getNextOrder(Integer learningPathId) {
        List<LearningNode> nodes = nodeRepository.findByLearningPathIdOrderByNodeOrderAsc(learningPathId);
        if (nodes.isEmpty()) return BigDecimal.ONE;
        return nodes.get(nodes.size() - 1).getNodeOrder().add(BigDecimal.ONE);
    }

    @Transactional
    public LearningNode saveNode(LearningNode node) {
        LearningNode saved = nodeRepository.save(node);
        autoNormalizeIfNeeded(node.getLearningPath().getId());
        return saved;
    }

    // UC14 - Chèn node giữa 2 node, order = (prev + next) / 2
    @Transactional
    public LearningNode insertNodeBetween(LearningNode newNode, BigDecimal prevOrder, BigDecimal nextOrder) {
        BigDecimal targetOrder = prevOrder.add(nextOrder)
                .divide(BigDecimal.valueOf(2), 9, RoundingMode.HALF_UP);
        newNode.setNodeOrder(targetOrder);
        return saveNode(newNode); // saveNode sẽ auto-normalize nếu cần
    }

    // Normalize thủ công (nút "Chuẩn hoá thứ tự")
    @Transactional
    public void normalizeNodeOrders(Integer learningPathId) {
        List<LearningNode> nodes = nodeRepository.findByLearningPathIdOrderByNodeOrderAsc(learningPathId);
        doNormalize(nodes);
    }

    // Tự động normalize khi khoảng cách giữa 2 node kề nhau < 0.001
    private void autoNormalizeIfNeeded(Integer learningPathId) {
        List<LearningNode> nodes = nodeRepository.findByLearningPathIdOrderByNodeOrderAsc(learningPathId);
        for (int i = 1; i < nodes.size(); i++) {
            BigDecimal gap = nodes.get(i).getNodeOrder().subtract(nodes.get(i - 1).getNodeOrder());
            if (gap.compareTo(NORMALIZE_THRESHOLD) < 0) {
                doNormalize(nodes);
                return;
            }
        }
    }

    private void doNormalize(List<LearningNode> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setNodeOrder(BigDecimal.valueOf(i + 1));
        }
        nodeRepository.saveAll(nodes);
    }

    @Transactional
    public void deleteNode(Integer id) {
        nodeRepository.deleteById(id);
    }

    // --- UC17: NODE CONTENT ---

    public List<NodeContent> getContentsByNode(Integer nodeId) {
        return contentRepository.findByLearningNodeIdOrderByDisplayOrderAsc(nodeId);
    }

    @Transactional
    public NodeContent saveContentToNode(Integer nodeId, NodeContent content) {
        LearningNode node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node không tồn tại!"));
        content.setLearningNode(node);
        return contentRepository.save(content);
    }

    @Transactional
    public void deleteContent(Integer contentId) {
        contentRepository.deleteById(contentId);
    }
}
