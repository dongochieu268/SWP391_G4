package com.edunac.mentora.controller;
import com.edunac.mentora.service.LearningNodeService;
import com.edunac.mentora.repository.LearningPathRepository;
import com.edunac.mentora.domain.NodeContent;
import com.edunac.mentora.domain.LearningPath;
import com.edunac.mentora.domain.LearningNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher/learning-path")
public class LearningNodeController {

    @Autowired
    private LearningNodeService nodeService;

    @Autowired
    private LearningPathRepository learningPathRepository;

    // UC14 - Xem danh sách node + nội dung
    @GetMapping("/{pathId}/nodes")
    public String viewNodes(@PathVariable Integer pathId, Model model) {
        LearningPath path = learningPathRepository.findById(pathId)
                .orElseThrow(() -> new IllegalArgumentException("Lộ trình không tồn tại"));

        List<LearningNode> nodes = nodeService.getNodesByPath(pathId);

        // Load nội dung của từng node vào Map để Thymeleaf truy xuất dễ
        Map<Integer, List<NodeContent>> contentMap = new LinkedHashMap<>();
        for (LearningNode node : nodes) {
            contentMap.put(node.getId(), nodeService.getContentsByNode(node.getId()));
        }

        model.addAttribute("path", path);
        model.addAttribute("nodes", nodes);
        model.addAttribute("contentMap", contentMap);
        model.addAttribute("pathId", pathId);
        model.addAttribute("nextOrder", nodeService.getNextOrder(pathId));
        return "teacher/nodes-list";
    }

    // UC14 - Lưu node (thêm cuối hoặc chèn giữa - order được tính sẵn từ JS)
    @PostMapping("/node/save")
    public String saveNode(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam BigDecimal nodeOrder,
            @RequestParam(required = false) Integer prerequisiteNodeId,
            @RequestParam Integer pathId
    ) {
        LearningPath path = learningPathRepository.getReferenceById(pathId);

        LearningNode node = new LearningNode();
        node.setTitle(title);
        node.setDescription(description);
        node.setNodeOrder(nodeOrder);
        node.setLearningPath(path);

        if (prerequisiteNodeId != null) {
            LearningNode prereq = new LearningNode();
            prereq.setId(prerequisiteNodeId);
            node.setPrerequisiteNode(prereq);
        }

        nodeService.saveNode(node);
        return "redirect:/teacher/learning-path/" + pathId + "/nodes";
    }

    // UC14 - Normalize thủ công thứ tự node
    @PostMapping("/{pathId}/normalize")
    public String normalize(@PathVariable Integer pathId) {
        nodeService.normalizeNodeOrders(pathId);
        return "redirect:/teacher/learning-path/" + pathId + "/nodes";
    }

    // UC14 - Xoá node
    @GetMapping("/node/delete/{id}")
    public String deleteNode(@PathVariable Integer id, @RequestParam Integer pathId) {
        nodeService.deleteNode(id);
        return "redirect:/teacher/learning-path/" + pathId + "/nodes";
    }
}
