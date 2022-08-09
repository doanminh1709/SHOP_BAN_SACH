package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.Category;
import com.example.qlybanhang.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/showCategory")
    public String showCategory(Model model) {
        model.addAttribute("showCategory", categoryRepository.findAll());
        return "content-page-admin-category";
    }

    @GetMapping("/admin/addCategory")
    public String addCategory() {
        return "category/add";
    }

    @PostMapping("/admin/addCategory")
    public String addCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/admin/showCategory";
    }

    @GetMapping("/admin/editCategory")
    public String editCategory(@RequestParam(name = "id", required = false) int idCategory, Model model) {
        Category category = categoryRepository.findCategoryById(idCategory);
        model.addAttribute("editCategory", category);
        return "category/edit";
    }

    @PostMapping("/admin/editCategory")
    public String editCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/admin/viewAdminShowCategory";
    }

    @GetMapping("/admin/deleteCategory")
    public String deleteCategory(@RequestParam(name = "id", required = false) int id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/viewAdminShowCategory";
    }
}
