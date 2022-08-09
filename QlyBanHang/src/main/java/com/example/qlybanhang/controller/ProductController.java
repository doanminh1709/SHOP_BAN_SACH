package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.Category;
import com.example.qlybanhang.Entity.Product;
import com.example.qlybanhang.repository.CategoryRepository;
import com.example.qlybanhang.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/productPagination")
    public String getProductByPagination(Model model,
                                         @RequestParam(name = "p", required = false) Optional<Integer> p) {
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Product> productPage = productRepository.findAll(pageable);
        model.addAttribute("pagePagination", productPage);
        return " ";
    }

    @GetMapping("/findAll")
    public String getFindAll(Model model) {
        model.addAttribute("listProduct", productRepository.findAll());
        return "product/view";
    }

    @GetMapping("/addProduct")
    public String createNewProduct(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/add";
    }

    @GetMapping("/admin/addProduct")
    public String adminCreateNewProduct(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "content-page-admin-product";
    }

    @PostMapping("/admin/addProduct")
    public String createNewProduct(@ModelAttribute Product product,
                                   @RequestParam(name = "filename", required = false) MultipartFile multipartFile,
                                   @RequestParam(name = "idCategory", required = false) int idCategory) {
        Category category = new Category();
        category.setId(idCategory);
        product.setCategory(category);
        if (multipartFile != null && multipartFile.getSize() > 0) {
            final String FOLDER = "G:/image/";
            String filename = multipartFile.getOriginalFilename();
            File file = new File(FOLDER + filename);
            try {
                multipartFile.transferTo(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            product.setImage("/download/file?filename=" + filename);
        }
        productRepository.save(product);
        return "redirect:/admin/viewAdminShowProduct";
    }

    @GetMapping("/download/file")
    public void downloadFile(HttpServletResponse response,
                             @RequestParam(name = "filename", required = false) String filename) throws IOException {
        final String FOLDER = "G:/image/";
        File file = new File(FOLDER + filename);
        if (file.exists()) {
            response.setHeader("Content-Disposition", "inline;filename=\"" + filename + "\"");
            response.setContentType("application/octet-stream;name=\"" + filename + "\"");//download to computer if neu file isn't pdf
            Files.copy(file.toPath(), response.getOutputStream());
        }
    }

    @GetMapping("/admin/editProduct")
    public String editProduct(@RequestParam(name = "id", required = false) int idProduct,
                              Model model) {
        Product product = productRepository.findById(idProduct).orElse(null);
        model.addAttribute("productById", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "content-page-admin-product";
    }

    @PostMapping("/admin/editProduct")
    public String editProduct(@ModelAttribute Product product,
                              @RequestParam(name = "filename", required = false) MultipartFile multipartFile) {
//        if (bindingResult.hasErrors()) {
//            return "product/edit";
//        } else {
        if (!multipartFile.isEmpty()) {
            String FOLDER = "G:/image/";
            String filename = multipartFile.getOriginalFilename();
            File file = new File(FOLDER + filename);
            try {
                multipartFile.transferTo(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            product.setImage("/download/file?filename=" + filename);
        }
        productRepository.save(product);
        return "redirect:/admin/viewAdminShowProduct";
//        }
    }

    @GetMapping("/admin/deleteProduct")
    public String removeProduct(@RequestParam(name = "id", required = false) int id) {
        Product product = productRepository.findProductById(id);
        if (product != null) {
            productRepository.deleteById(id);
        }
        return "redirect:/admin/viewAdminShowProduct";
    }

    @GetMapping("/divideProducts")
    public String getProductByCategory(Model model) {
        model.addAttribute("newBook", productRepository.searchByCategoryName("mới"));
        model.addAttribute("hotBook", productRepository.searchByCategoryName("hot"));
        model.addAttribute("goodBook", productRepository.searchByCategoryName("hay"));
        model.addAttribute("comingSoonBook", productRepository.searchByCategoryName("sắp phát hành"));
        return "product/viewProductByCategory";
    }

    /*
        //The way one
        @PostMapping("/searchByName")
        public String searchProductByName(Model model,
                                          @RequestParam(name = "name", required = false) String name,
                                          @RequestParam(name = "page", required = false) String page,
                                          @RequestParam(name = "size", required = false) String size) {
            int currentPage = 0;
            int currentSize = 5;
            if (StringUtils.hasText(page)) {
                currentPage = Integer.parseInt(page);
            }
            if (StringUtils.hasText(size)) {
                currentSize = Integer.parseInt(size);
            }
            Pageable pageable = PageRequest.of(currentPage, currentSize);
    //        if (StringUtils.hasText(name)) {
    //            Page<Product> productByName = productRepository.searchByName("%" + name + "%", pageable);
                model.addAttribute("totalPage", productByName.getTotalPages());
                model.addAttribute("productList", productByName.getContent());
                model.addAttribute("name", name);
                return "product/resultSearch";
    //        } else {
    //            return "product/notFound";
    //        }
        }
    */
    @PostMapping("/searchByName")
    public String searchProductByName(Model model,
                                      @RequestParam(name = "name", required = false) String name) {
        if (StringUtils.hasText(name)) {
            Product productByName = productRepository.searchByName("%" + name + "%");
            model.addAttribute("productList", productByName);
            return "product/resultSearch";
        } else {
            return "product/notFound";
        }
    }

    //The way two
    @PostMapping("/search_product_by_name")
    public String search(Model model, @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "page", required = false) Integer page,
                         @RequestParam(name = "size", required = false) Integer size) {
        if (size == null) {//max records per page
            size = 3;
        }
        if (page == null) {
            page = 0; //current size
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());
        if (name != null) {
            Page<Product> pageProduct = productRepository.searchByNameProduct("%" + name + "%", pageable);
            model.addAttribute("list", pageProduct.toList());
            model.addAttribute("totalPage", pageProduct.getTotalPages());
        } else {
            Page<Product> pageProduct = productRepository.findAll(pageable);
            model.addAttribute("list", pageProduct.toList());
            model.addAttribute("totalPage", pageProduct.getTotalPages());
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("name", name == null ? "" : name);
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "product/resultSearch";
    }

}
