package com.edunac.mentora.controller;
import com.edunac.mentora.service.LearningNodeService;
import com.edunac.mentora.domain.NodeContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher/learning-path/node-content")
public class NodeContentController {

    @Autowired
    private LearningNodeService nodeService;

    @PostMapping("/save")
    public String saveContent(@ModelAttribute NodeContent content,
                              @RequestParam Integer nodeId,
                              @RequestParam Integer pathId) {

        nodeService.saveContentToNode(nodeId, content);

        return "redirect:/teacher/learning-path/" + pathId + "/nodes";
    }

    @GetMapping("/delete/{contentId}")
    public String deleteContent(@PathVariable Integer contentId,
                                @RequestParam Integer pathId) {

        nodeService.deleteContent(contentId);

        return "redirect:/teacher/learning-path/" + pathId + "/nodes";
    }
}