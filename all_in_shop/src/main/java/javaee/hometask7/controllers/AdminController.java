package javaee.hometask7.controllers;

import javaee.hometask7.entities.*;
import javaee.hometask7.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(){
        return "redirect:/admin/items";
    }

    @GetMapping(value = "/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String categories(Model model, HttpServletRequest request) {
        lng_config(request);
        List<Categories> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin_categories";
    }

    @GetMapping(value = "/brands")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String brands(Model model,  HttpServletRequest request) {
        lng_config(request);
        List<Brands> brands = brandService.getAllBrands();
        List<Countries> countries = countryService.getAllCountries();
        model.addAttribute("brands", brands);
        model.addAttribute("countries", countries);
        return "admin_brands";
    }

    @GetMapping(value = "/countries")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String countries(Model model,  HttpServletRequest request) {
        lng_config(request);
        List<Countries> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);
        return "admin_countries";
    }

    @GetMapping(value = "/items")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String items(Model model,  HttpServletRequest request){
        lng_config(request);
        List<Items> items = itemService.getAllItems();
        List<Brands> brands = brandService.getAllBrands();
        List<Categories> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("items", items);
        return "admin_items";
    }

    @PostMapping(value = "/add_brand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addBrand(@RequestParam("name") String name,
                             @RequestParam("country") Countries country){
        brandService.addBrand(new Brands(null, name, country));
        return "redirect:/admin/brands";
    }
    @PostMapping(value = "/edit_brand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editBrand(@RequestParam("name") String name,
                              @RequestParam("country") Long country_id,
                              @RequestParam("id") Long id) {
        brandService.saveBrand(new Brands(id, name, countryService.getCountry(country_id)));
        return "redirect:/admin/brands";
    }

    @GetMapping(value = "/delete_brand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteBrand(@RequestParam("id") Long id){
        brandService.deleteBrand(brandService.getBrand(id));
        return "redirect:/admin/brands";
    }
    @PostMapping(value = "/add_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategory(@RequestParam("name") String name,
                             @RequestParam("logo_url") String logo_url){
        categoryService.addCategory(new Categories(null,name,logo_url));
        return "redirect:/admin/categories";
    }
    @PostMapping(value = "/edit_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCategory(@RequestParam("name") String name,
                              @RequestParam("logo_url") String logoUrl,
                              @RequestParam("id") Long id) {
        categoryService.saveCategory(new Categories(id, name, logoUrl));
        return "redirect:/admin/categories";
    }

    @GetMapping(value = "/delete_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCategory(@RequestParam("id") Long id){
        categoryService.deleteCategory(categoryService.getCategory(id));
        return "redirect:/admin/categories";
    }

    @PostMapping(value = "/add_country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCountry(@RequestParam("name") String name,
                             @RequestParam("code") String code){
        countryService.addCountry(new Countries(null, name, code));
        return "redirect:/admin/countries";
    }
    @PostMapping(value = "/edit_country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCountry(@RequestParam("name") String name,
                              @RequestParam("code") String code,
                              @RequestParam("id") Long id) {
        countryService.saveCountry(new Countries(id, name, code));
        return "redirect:/admin/countries";
    }

    @GetMapping(value = "/delete_country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCountry(@RequestParam("id") Long id){
        countryService.deleteCountry(countryService.getCountry(id));
        return "redirect:/admin/countries";
    }



    @GetMapping(value = "/delete_item")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@RequestParam("id") Long id) {
        itemService.deleteItem(itemService.getItem(id));
        return "redirect:/admin/items";
    }

    @PostMapping(value = "/add_item")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add_item(@RequestParam("name") String name,
                           @RequestParam("desc") String desc,
                           @RequestParam("price") double price,
                           @RequestParam("in_top_page") boolean in_top_page,
                           @RequestParam("small_pic_url") String small_pic,
                           @RequestParam("large_pic_url") String large_pic,
                           @RequestParam("rating") int stars,
                           @RequestParam("brand_id") Long brand_id,
                           @RequestParam("categories") List<Long> cat_ids
    ) {
        List<Categories> categories = new ArrayList<>();
        for (Long id: cat_ids) {
            categories.add(categoryService.getCategory(id));
        }
        itemService.addItem(new Items(null, name, desc, price,stars,small_pic,
                large_pic, new Date(),in_top_page,brandService.getBrand(brand_id),categories));
        return "redirect:/admin/items";
    }

    @PostMapping(value = "/edit_item")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(@RequestParam("name") String name,
                       @RequestParam("desc") String desc,
                       @RequestParam("price") double price,
                       @RequestParam("in_top_page") boolean in_top_page,
                       @RequestParam("small_pic_url") String small_pic,
                       @RequestParam("large_pic_url") String large_pic,
                       @RequestParam("rating") int stars,
                       @RequestParam("id") Long id,
                       @RequestParam("brand") Long brand
    ) {
        Items item = new Items(id, name, desc, price, stars, small_pic, large_pic,
                itemService.getItem(id).getAddedDate(), in_top_page, brandService.getBrand(brand), itemService.getItem(id).getCategories());
        itemService.saveItem(item);
        return "redirect:/admin/items";
    }

    @GetMapping(value = "/details/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String details(Model model, @PathVariable(name = "id") String _id, HttpServletRequest request) {
        Long id = Long.parseLong(_id.split("_")[0]);
        Items item = itemService.getItem(id);
        List<Categories> item_cats = item.getCategories();
        List<Brands> brands = brandService.getAllBrands();
        List<Categories> categories = categoryService.getAllCategories();
        boolean rem = categories.removeAll(item_cats);
        lng_config(request);
        model.addAttribute("item", item);
        model.addAttribute("item_cats", item_cats);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);

        return "admin_details";
    }
    @GetMapping(value = "/assign_cat")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignCat(@RequestParam("id") Long id,
                            @RequestParam("cat_id") Long cat_id){
        Categories category = categoryService.getCategory(cat_id);
        Items item = itemService.getItem(id);
        List<Categories> categories = item.getCategories();
        categories.add(category);
        item.setCategories(categories);
        itemService.saveItem(item);

        return "redirect:/admin/details/"+id+'_'+item.getName().replaceAll(" ","-");

    }
    @GetMapping(value = "/unassign_cat")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String unassignCat(@RequestParam("id") Long id,
                            @RequestParam("cat_id") Long cat_id){
        Categories category = categoryService.getCategory(cat_id);
        Items item = itemService.getItem(id);
        List<Categories> categories = item.getCategories();
        categories.remove(category);
        item.setCategories(categories);
        itemService.saveItem(item);

        return "redirect:/admin/details/"+id+'_'+item.getName().replaceAll(" ","-");

    }
    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;

    }

    public void lng_config(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String lng_param = request.getParameter("lng");
        request.setAttribute("user", getUserData());
        if (lng_param != null) {
            request.setAttribute("lng", lng_param);
        } else {
            for (Cookie c : cookies) {
                if (c.getName().equals("language")) {
                    request.setAttribute("lng", c.getValue());
                    break;
                }
            }
        }

    }
}
