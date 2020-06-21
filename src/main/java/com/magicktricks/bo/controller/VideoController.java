package com.magicktricks.bo.controller;

import com.google.firebase.database.*;
import com.magicktricks.bo.dto.UpdateVideoDto;
import com.magicktricks.bo.entities.Video;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abderrahman.boudrai on 06/06/2020
 */
@Controller
public class VideoController {

    public static volatile Boolean callBackReturned = null;
    public static volatile Boolean videoReturned = null;
    private Thread firebaseThread;

    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public ModelAndView getVideos(Model model){
        final List<Video> allVideos = this.getAllVideos();
        while (callBackReturned == null){

        }
        model.addAttribute("videos",allVideos);

        return new ModelAndView("/videos","updateVideoDto",new UpdateVideoDto());


    }

    @PostMapping(value = "/deleteVideo")
    public ModelAndView deleteVideo(@ModelAttribute("updateVideoDto")UpdateVideoDto updateVideoDto,Model model){
        final String videoId = updateVideoDto.getVideoId();

        videoReturned = null;
        this.removeVideoFromDB(videoId);

        while (videoReturned == null){

        }
        callBackReturned = null;
        final List<Video> videos = this.getAllVideos();
        while(callBackReturned == null) {

        }
        model.addAttribute("videos",videos);


        return new ModelAndView("/videos","updateVideosDto",new UpdateVideoDto());
    }


    private List<Video> getAllVideos() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("videos");
        final List<Video> result = new ArrayList();

        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    result.add(data.getValue(Video.class));
                }
                VideoController.callBackReturned = !(CollectionUtils.isEmpty(result));
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return result;

    }

    private void removeVideoFromDB(final String videoId){
        {
            this.firebaseThread = new Thread(() -> {
                final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference("videos");
                final Query query = databaseReference.orderByChild("uploadId").equalTo(videoId);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue((databaseError, databaseReference1) -> VideoController.videoReturned = Boolean.TRUE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            });
            this.firebaseThread.start();
        }

    }

}
