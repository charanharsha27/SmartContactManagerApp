package com.smart.Controller;

import com.smart.helper.Message;
import com.smart.model.Orders;
import com.smart.model.User;
import com.smart.service.IOrderService;
import com.smart.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

import org.json.JSONObject;
import com.razorpay.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService ;

    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String,Object> data, Principal principal) throws RazorpayException
    {
        User user = userService.getData(principal.getName());
        System.out.println(user);
        System.out.println(data);

        int amount = Integer.parseInt(data.get("amount").toString());

        var client = new RazorpayClient("rzp_test_GxHBH51k6keEAC", "jdbyYImYvgxV3j9t9Bd1eDui");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount",amount*100);
        jsonObject.put("currency","INR");
        jsonObject.put("receipt","txn_279532");


        Order order = client.orders.create(jsonObject);
        System.out.println(order);

        Orders myOrder = new Orders();

        myOrder.setOrderId(order.get("id").toString());
        myOrder.setAmount(order.get("amount").toString());
        myOrder.setPaymentId(null);
        myOrder.setStatus("created");
        myOrder.setUser(user);
        myOrder.setReceipt(order.get("receipt"));

        orderService.saveOrder(myOrder);

        return order.toString();
    }

    @PostMapping("/update-order")
    public String updateOrder(@RequestBody Map<String,Object> data, Principal principal, Map<String,Object> map, HttpSession session)
    {
        User user = userService.getData(principal.getName());
        System.out.println(user);
        System.out.println(data);

        Orders myOrder = orderService.getOrder(data.get("order_id").toString());
        myOrder.setPaymentId(data.get("payment_id").toString());
        myOrder.setStatus(data.get("statusx").toString());
        System.out.println(data);
        orderService.saveOrder(myOrder);
        System.out.println("order success");
        map.put("user",user);
        return "user/contribute";
    }
}
