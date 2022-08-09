package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.*;
import com.example.qlybanhang.dto.OrderDTO;
import com.example.qlybanhang.dto.OrderItemDTO;
import com.example.qlybanhang.repository.CouponRepository;
import com.example.qlybanhang.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class CartController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    private double total = 0;

    @GetMapping(value = {"/view-to-cart"})
    public String cart(HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");

        OrderDTO orderDTO = (OrderDTO) session.getAttribute("cart");
        if (orderDTO != null) {
                request.setAttribute("total", session.getAttribute("total"));
                request.setAttribute("order", orderDTO);
                if (session.getAttribute("coupon") != null) {
                    Coupon coupon = (Coupon) session.getAttribute("coupon");
                    request.setAttribute("coupon", coupon);
                }
                return "cart/view";
        } else {
            return "cart/viewCartEmpty";
        }
    }

    @GetMapping("/cart-empty")
    public String cartEmpty() {
        return "cart/cart_empty";
    }


    @GetMapping("/add-to-cart")
    public String addProductToCart(HttpSession session,
                                   @RequestParam(name = "idProduct") int idProduct,
                                   Model model) {
        Product product = productRepository.findProductById(idProduct);
        if (session.getAttribute("cart") == null) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            OrderDTO orderDTO = new OrderDTO();
            orderItemDTO.setNumber(1);
            orderItemDTO.setProduct(product);
            total += orderItemDTO.getProduct().getPrice();
            List<OrderItemDTO> itemDTOs = new ArrayList<>();
            itemDTOs.add(orderItemDTO);
            orderDTO.setOrderItemList(itemDTOs);

            session.setAttribute("cart", orderDTO);

        } else {
            OrderDTO orderDTO = (OrderDTO) session.getAttribute("cart");
            List<OrderItemDTO> itemDTOs = orderDTO.getOrderItemList();
            boolean flag = false;
            for (OrderItemDTO orderItemDTO : itemDTOs) {
                if (orderItemDTO.getProduct().getId() == product.getId()) {
                    total += orderItemDTO.getProduct().getPrice();
                    orderItemDTO.setNumber(orderItemDTO.getNumber() + 1);
                    flag = true;
                }
            }
            if (!flag) {
                OrderItemDTO orderItemDTO = new OrderItemDTO();
                orderItemDTO.setNumber(1);
                orderItemDTO.setProduct(product);
                total += orderItemDTO.getProduct().getPrice();
                itemDTOs.add(orderItemDTO);
                orderDTO.setOrderItemList(itemDTOs);
            }
            session.setAttribute("cart", orderDTO);
        }
        OrderDTO orderDTO = (OrderDTO) session.getAttribute("cart");
        session.setAttribute("total", total);
        model.addAttribute("order", orderDTO);
        return "redirect:/view-to-cart";
    }

    @GetMapping("/deleteProductToCart/{idProduct}")
    public String deleteProductToCart(HttpSession session,
                                      @PathVariable("idProduct") int idProduct) {
        if (session.getAttribute("cart") != null) {
            OrderDTO orderDTO = (OrderDTO) session.getAttribute("cart");
            List<OrderItemDTO> orderItemList = orderDTO.getOrderItemList();
            for (int i = 0; i < orderItemList.size(); i++) {
                if (orderItemList.get(i).getProduct().getId() == idProduct) {
                    total -= orderItemList.get(i).getProduct().getPrice() * orderItemList.get(i).getNumber();
                    System.out.println(total);
                    orderItemList.remove(i);
                }
            }
            orderDTO.setOrderItemList(orderItemList);
            if (orderItemList.isEmpty()) {
                session.setAttribute("total", 0);
                session.removeAttribute("cart");
            } else {
                session.setAttribute("total", total);
                session.setAttribute("cart", orderDTO);
            }
        }
        return "redirect:/view-to-cart";
    }

    @PostMapping("/editToCart")
    public String updateToCart(HttpServletRequest request, HttpSession session,
                               @RequestParam(name = "idProduct", required = false) int idProduct,
                               @RequestParam(name = "count", required = false) int quality) {

        if (session.getAttribute("cart") != null) {
            OrderDTO orderDTO = (OrderDTO) session.getAttribute("cart");
            List<OrderItemDTO> orderItemList = orderDTO.getOrderItemList();
            for (OrderItemDTO orderItemDTO : orderItemList) {
                if (orderItemDTO.getProduct().getId() == idProduct) {
                    total = total - (orderItemDTO.getProduct().getPrice() * orderItemDTO.getNumber())
                            + (orderItemDTO.getProduct().getPrice() * quality);
                    orderItemDTO.setNumber(quality);
                }
            }
            session.setAttribute("total", total);
            session.setAttribute("cart", orderDTO);
            request.setAttribute("order", session.getAttribute("cart"));
        }
        return "redirect:/view-to-cart";
    }

    @PostMapping("/enter_discount_code")
    public String checkDiscountCode(HttpSession session, Model model,
                                    @RequestParam(name = "coupon", required = false) String coupon, RedirectAttributes attributes) {
        if (session.getAttribute("coupon") == null) {
            AtomicBoolean mark = new AtomicBoolean(false);
            couponRepository.findAll().forEach(item -> {
                if (item.getCouponCode().compareTo(coupon) == 0) {
                    mark.set(true);
                    double amountIsReduced = (item.getDiscountAmount() * total) / 100;
                    total -= amountIsReduced;
                    session.setAttribute("coupon", item);
                    model.addAttribute("coupon_success", "Được giảm giá :" + amountIsReduced + "VNĐ");
                } else {
                    session.removeAttribute("coupon");
                }
            });
            if (mark.get()) {
                session.setAttribute("total", total);
            } else {
                attributes.addFlashAttribute("couponNotExists", "Không tồn tại mã này");
            }
        }
        return "redirect:/view-to-pay";
    }
}
