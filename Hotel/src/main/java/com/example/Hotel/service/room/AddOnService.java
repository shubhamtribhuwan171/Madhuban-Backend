package com.example.Hotel.service.room;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.room.AddOns;
import com.example.Hotel.entity.room.AddOnsDto;
import com.example.Hotel.repository.room.AddOnRepository;

import jakarta.transaction.Transactional;

@Service
public class AddOnService {
    
    @Autowired
    AddOnRepository addOnRepository;

    @Transactional
    //add new addOn
    public void addAddOn(AddOnsDto addOnsDto)
    {
        String name = addOnsDto.getName();
        Double cost = addOnsDto.getCost();
        AddOns addOn = new AddOns(name, cost);
        addOnRepository.save(addOn);
    }
    //get all add ons available
    public List<AddOnsDto> getAllAddOns()
    {
        List<AddOns> list = addOnRepository.findAll();
        return mapToDtoList(list);
    }
    //get cost by add name
    public Double getCostByAddOnName(String addOnName)
    {
        Optional<AddOns> optional = addOnRepository.findByName(addOnName);
        if(optional.isPresent())
        {
            AddOns addOns = optional.get();
            return addOns.getCost();
        }
        return  0d;
    }
    //update Cost by add on name
    public void updateCostByAddOnName(String addOnName, Double newCost)
    {
        Optional<AddOns> optional = addOnRepository.findByName(addOnName);
        if(optional.isPresent())
        {
            AddOns addOns = optional.get();
            addOns.setCost(newCost);
            addOnRepository.save(addOns);
        }
    }
    @Transactional
    //delete add on by name
    public void deleteByAddOnName(String addOnName)
    {
        addOnRepository.deleteByName(addOnName);
    }
    //map obj to dto
    private static AddOnsDto mapToDto(AddOns addOns)
    {
        String name = addOns.getName();
        Double cost = addOns.getCost();
        AddOnsDto addOnDto = new AddOnsDto(name, cost.toString());
        return addOnDto;
    }
    //map dto to obj
   /*  private static AddOns mapToObj(AddOnsDto addOnsDto)
    {
        String name = addOnsDto.getName();
        Double cost = addOnsDto.getCost();
        AddOns addOn = new AddOns(name, cost);
        return addOn;
    }*/
    //map obj list to dto list
    private static List<AddOnsDto> mapToDtoList(List<AddOns> list)
    {
        return list.stream()
            .map(AddOnService::mapToDto)
            .collect(Collectors.toList());
    }
    //map dto list to obj list
    
}
