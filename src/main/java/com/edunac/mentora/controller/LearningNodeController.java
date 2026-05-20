package com.edunac.mentora.controller;

import com.edunac.mentora.domain.LearningNode;
import com.edunac.mentora.service.LearningNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher/learning-path")
public class LearningNodeController {

    @Autowired
    private LearningNodeService nodeService;

    @GetMapping("/{pathId}/nodes")
    public String viewNodes(@PathVariable Integer pathId, Model model) {
        List<LearningNode> nodes = nodeService.getNodesByPath(pathId);

        model.addAttribute("nodes", nodes);
        model.addAttribute("pathId", pathId);

        return "teacher/nodes-list";
    }

    @PostMapping("/node/save")
    public String saveNode(@ModelAttribute LearningNode node, @RequestParam Integer pathId) {
        nodeService.saveNode(node);
        return "redirect:/teacher/learning-path/" + pathId + "/nodes";
    }

    @GetMapping("/node/delete/{id}")
    public String deleteNode(@PathVariable Integer id, @RequestParam Integer pathId) {
        nodeService.deleteNode(id);
        return "redirect:/teacher/learning-path/" + pathId + "/nodes";
    }
}