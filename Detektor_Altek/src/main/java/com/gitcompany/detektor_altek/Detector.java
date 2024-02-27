/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitcompany.detektor_altek;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageHistory.MessageRetrieveAction;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 *
 * @author kkile
 */
public class Detector {
    private JDA bot;
    private MainFrame frame;
    private List<CustomMember> membersList;
    private MessageReceivedEvent core;
    private MapaMysli mapaMysli;
    
    public Detector(MainFrame frame){
        String token = ""; //<- TOKEN
        this.frame = frame;
        mapaMysli = new MapaMysli();
        bot = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGE_TYPING)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new MyListener(this))
                .build();
    }
    
    public MapaMysli getMapaMysli(){
        return mapaMysli;
    }
    
    public void serverChoosen(MessageReceivedEvent event){
        core = event;
                
        event.getChannel().asTextChannel().sendMessage("Loading messages...").queue();
        
        frame.getServerNameLabel().setText(event.getGuild().getName());
        membersList = new ArrayList<CustomMember>();
        List<Member> temp = event.getGuild().getMembers();
        
        MessageHistory lastHistory = null;
        MessageHistory history = MessageHistory.getHistoryFromBeginning(event.getChannel()).complete();
        List<Message> msgList = new ArrayList<Message>();

        Map<String, List<String>> allMessages = new HashMap<String, List<String>>();

        do{
           msgList = new ArrayList<Message>();
           lastHistory = history; 
           for(int x = 0; x < history.getRetrievedHistory().size(); x++){
              msgList.add(history.getRetrievedHistory().get(x));
              Message currMsg = msgList.get(x);

              if(currMsg.getMember() != null){
                  String memberId = currMsg.getMember().getId();
                  if(!allMessages.containsKey(memberId)){
                      allMessages.put(memberId, new ArrayList<>());
                  }
                  allMessages.get(memberId).add(currMsg.getContentRaw());
              }

            }
           if(msgList.size() == 0){
               break;
           }
           history = MessageHistory.getHistoryAfter(event.getChannel(), msgList.get(0).getId()).complete();
        }
        while(!history.equals(lastHistory));
        
        frame.getDialog().setVisible(false);
        for(int x = 0; x < temp.size(); x++){
            membersList.add(new CustomMember(temp.get(x), allMessages.get(temp.get(x).getId())));
        }
        
        DefaultListModel dlm = new DefaultListModel();
        for(CustomMember member : membersList){
            dlm.addElement(member.getName());
        }
        frame.getList().setModel(dlm);
        event.getChannel().asTextChannel().sendMessage("Messages loaded! Time to ban some alt bitches!!!").queue();
    }
    
    public List<CustomMember> getMembersList(){
        return membersList;
    }
    
    public void ban(int idInList){
        CustomMember member = membersList.get(idInList);
        TextChannel channel = core.getChannel().asTextChannel();
        
        for(String msg : member.getMessages()){
            System.out.println(msg);
        }
        
        channel.sendMessage("Banning user " + member.getName()).queue();
        channel.sendMessage("Reason = Being FUCKING alternatywka").queue();

        Member temp = null;
        for(Member m : core.getGuild().getMembers()){
            if(m.getEffectiveName().equals(member.getName())){
                temp = m;
            }
        }
        UserSnowflake user = temp;
        core.getGuild().ban(user, 7, TimeUnit.DAYS).queue();
    }
    
    public void invest(int idinList){
        CustomMember member = membersList.get(idinList);
        List<String> allMessages = member.getMessages();
        List<String> eachWord = new ArrayList<>();
        
        for(int x = 0; x < allMessages.size(); x++){
            String[] temp = allMessages.get(x).split("[\s,.]+");
            for( int y = 0; y < allMessages.size(); y++){
                eachWord.add(temp[y]);
            }
        }
        
        double out = mapaMysli.SiecNeuronowa(eachWord);
        
        core.getChannel().asTextChannel().sendMessage("Propability of being fucking alternatywka for " + member.getName() + " = " + out).queue();
    }
}
class MyListener extends ListenerAdapter{
    Random r;
    private Detector detector;
    
    public MyListener(Detector detector){
        r = new Random();
        this.detector = detector;
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //379716226311520258 <- Moje ID
        if(event.getMember().getId().equals("379716226311520258")){
            String msg = event.getMessage().getContentRaw();
            
            if(msg.equals("-Start")){
                detector.serverChoosen(event);
            }
            
            
//            if(msg.contains("-Investigate")){
//                String id = msg.replaceAll("-Investigate ", "").substring(2, msg.replaceAll("-Investigate ", "").length() - 1);
//                Member member = event.getGuild().getMemberById(id);
//                MessageHistory lastHistory = null;
//                MessageHistory history = MessageHistory.getHistoryFromBeginning(channel).complete();
//                List<Message> msgList = new ArrayList<Message>();
//                
//                Map<String, List<String>> allMessages = new HashMap<String, List<String>>();
//                
//                do{
//                   msgList = new ArrayList<Message>();
//                   lastHistory = history; 
//                   for(int x = 0; x < history.getRetrievedHistory().size(); x++){
//                      msgList.add(history.getRetrievedHistory().get(x));
//                      Message currMsg = msgList.get(x);
//                      
//                      if(currMsg.getMember() != null){
//                          String memberId = currMsg.getMember().getId();
//                          if(!allMessages.containsKey(memberId)){
//                              allMessages.put(memberId, new ArrayList<>());
//                          }
//                          allMessages.get(memberId).add(currMsg.getContentRaw());
//                      }
//                      
//                    }
//                   System.out.println(msgList.size());
//                   System.out.println("--------------------------------------------");
//                   if(msgList.size() == 0){
//                       break;
//                   }
//                   history = MessageHistory.getHistoryAfter(channel, msgList.get(0).getId()).complete();
//                }
//                while(!history.equals(lastHistory));
//                
//                
//                List<String> memberMsgs = allMessages.get(id);
//
//                channel.sendMessage("User " + event.getGuild().getMemberById(id).getEffectiveName() + " has sent " + memberMsgs.size() + " messages.").queue();
//                channel.sendMessage("Estimated AltkoRate = " + 0.925 + " -> High propability of being jebaną altką").queue();
//
//            }
//            else if(msg.contains("-DDOX")){
//                String id = msg.replaceAll("-DDOX ", "").substring(2, msg.replaceAll("-DDOX ", "").length() - 1);
//                channel.sendMessage("...Proceeding to gather informations...").queue();
//                
//                Member member = event.getGuild().getMemberById(id);
//                
//                String[] messages = {"Suspects name: " + member.getEffectiveName(), "Suspects IP: " + randomIP(), "Suspects Adress: " + randomAdress(), "Suspects status: " + member.getOnlineStatus().toString()}; 
//                
//                for(int x = 0; x < messages.length; x++){
//                    channel.sendMessage(messages[x]).queue();
//                }
//            }
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
