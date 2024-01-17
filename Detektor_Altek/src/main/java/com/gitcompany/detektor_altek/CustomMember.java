/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitcompany.detektor_altek;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.utils.ImageProxy;

/**
 *
 * @author egzamin
 */
public class CustomMember {
    private String id, name, status;
    private ImageProxy avatar;
    private List<String> roles;
    
    public CustomMember(Member member){
        id = member.getId();
        name = member.getEffectiveName();
        status = member.getOnlineStatus().name();
        avatar = member.getAvatar();
        
        List<String> temp = new ArrayList<String>();
        for(Role role : member.getRoles()){
            temp.add(role.getName());
        }
    }
    
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getStatus(){
        return status;
    } 
    public ImageProxy getAvatar(){
        return avatar;
    }
    public List<String> getRoles(){
        return roles;
    }
}
