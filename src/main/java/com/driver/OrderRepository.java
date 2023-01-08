package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private HashMap<String,Order> orderMap;
    private HashMap<String,DeliveryPartner> partnerMap;
    private HashMap<String, List<Order>> orderPartnerMap;
    HashMap<String,Order> unassignedOrder=new HashMap<>();

    public OrderRepository() {
      this.orderMap = new HashMap<>();
      this.partnerMap = new HashMap<>();
      this.orderPartnerMap = new HashMap<>();
    }

    public void addOrder(Order order){
        orderMap.put(order.getId(), order);
    }

    public void addPartner(String partnerId){
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,partner);
    }

    public void addOrderPartnerPair(String orderId,String partnerId){
        Order order = orderMap.get(orderId);
        if(orderPartnerMap.containsKey(partnerId)){
            List<Order> list = orderPartnerMap.get(partnerId);
            list.add(order);
            orderPartnerMap.put(partnerId,list);
        }else {
            List<Order> list = new ArrayList<>();
            list.add(order);
            orderPartnerMap.put(partnerId,list);
        }
    }

    public Order getOrderById(String orderId){
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return partnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        List<Order> list = orderPartnerMap.get(partnerId);
        return list.size();

        //return orderPartnerMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
         List<String> result=new ArrayList<>();

        List<Order> orders=orderPartnerMap.get(partnerId);

        for(Order o:orders){
            result.add(o.getId());
        }
        return result;
    }

    public List<String> getAllOrders(){
        List<String> orders=new ArrayList<>();
        for(String s:orderMap.keySet()){
            orders.add(s);
        }
        return orders;
    }


    public int getCountOfUnassignedOrders(){
//        int count=0;
//        for(String s:orderHashMap.keySet()){
//            for(List<Order> orders:orderPartnerPairHashMap.values()){
//                for(Order o:orders){
//                    if(o.getId().equals(s)){
//                        count++;
//                    }
//                }
//            }
//
//        }
//        return orderHashMap.size()-count;
        return unassignedOrder.size();
    }


    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){

        String hr=time.substring(0,2);
        String min=time.substring(3,5);

        int totalTime=(Integer.parseInt(hr)*60)+Integer.parseInt(min);
        int count=0;
        for(Order o:orderPartnerMap.get(partnerId)){
            if(o.getDeliveryTime()>totalTime){
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int maxTime=Integer.MIN_VALUE;
        for(Order o:orderPartnerMap.get(partnerId)){
            if(o.getDeliveryTime()>maxTime)
                maxTime=o.getDeliveryTime();
        }
        int hr=maxTime/60;
        int min=maxTime%60;
        String str=hr+":"+min;
        System.out.println(str);

        return str;

    }

    public void deletePartnerById(String partnerId){
        for(Order o:orderPartnerMap.get(partnerId)){
            unassignedOrder.put(o.getId(),o);
        }
        orderPartnerMap.remove(partnerId);
        partnerMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        for(String s:orderPartnerMap.keySet()){
            List<Order> list=orderPartnerMap.get(s);
            Iterator<Order> iterator=list.iterator();

//            for(Order o:list){
//                if(o.getId().equals(orderId)){
//                    list.remove(o);
//                }
//            }
            while(iterator.hasNext()){
                Order o=iterator.next();
                if(o.getId().equals(orderId)){
                    iterator.remove();
                }
            }

        }
        orderMap.remove(orderId);
    }
}
