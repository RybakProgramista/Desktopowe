/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitcompany.detektor_altek;

import java.util.Random;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

/**
 *
 * @author kkile
 */
public class Detector {
    private JDA bot;
    public Detector(){
        String token = "MTE4NzMyNDQwODY5MjQ4MjA5OA.GxV6dH.li0NB3eQnO9EcQqfEoudkMyl_XxhHEdcS14BaQ"; //<- TOKEN
        bot = JDABuilder.createDefault(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGE_TYPING)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new MyListener())
                .build();
    }
}
class MyListener extends ListenerAdapter{
    Random r;
    
    public MyListener(){
        r = new Random();
        System.out.println(randomIP());
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //379716226311520258 <- Moje ID
        if(event.getMember().getId().equals("379716226311520258")){
            String msg = event.getMessage().getContentRaw();
            MessageChannelUnion channel = event.getChannel();
            
            
            if(msg.equals("-Investigate")){
                
            }
            else if(msg.contains("-DDOX")){
                String id = msg.replaceAll("-DDOX ", "").substring(2, msg.replaceAll("-DDOX ", "").length() - 1);
                channel.sendMessage("...Proceeding to gather informations...").queue();
                
                Member member = event.getGuild().getMemberById(id);
                
                String[] messages = {"Suspects name: " + member.getEffectiveName(), "Suspects IP: " + randomIP(), "Suspects Adress: " + randomAdress(), "Suspects status: " + member.getOnlineStatus().toString()}; 
                
                for(int x = 0; x < messages.length; x++){
                    channel.sendMessage(messages[x]).queue();
                }
            }
        }
    }
    String randomIP(){
       return "" + (r.nextInt(72) + 11) + "." + (r.nextInt(899) + 100) + "." + (r.nextInt(899) + 100);
    }
    String randomAdress(){
        String adress = "";
        
        String[] postalCodes = {"67-100 Nowa Sól", "43-400 Cieszyn", "67-120 Kożuchów", "10-334 Olsztyn", "67-416 Konotop"};
        adress = postalCodes[r.nextInt(postalCodes.length)] + " ";
        
        String[] streetsName = {"os Konstytucji 3-ego Maja", "ul Wojska Polskiego", "ul Jałowcowa", "Plac Wyzwolenia", "ul Wiśniowa"};
        adress += streetsName[r.nextInt(streetsName.length)] + " ";
        
        String[] letters = {"A", "B", "C", "D"};
        adress += (r.nextInt(11) + 1) + letters[r.nextInt(letters.length)];
        
        return adress;
    }
}
