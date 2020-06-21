package com.magicktricks.bo.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.magicktricks.bo.dto.UpdateUserDto;
import com.magicktricks.bo.dto.UserLoginDto;
import com.magicktricks.bo.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by abderrahman.boudrai on 23/05/2020
 */
@Controller public class HomeController {

    public static volatile Boolean callBackReturned = null;
    public static volatile Boolean userReturned = null;

    private Thread firebaseThread;

    @RequestMapping(value = "/")
    public String home(final Model model) throws IOException {
	final String fileName = "serviceAccountKey.json";
	final ClassLoader classLoader = this.getClass().getClassLoader();

	final File file = new File(classLoader.getResource(fileName).getFile());

	final FileInputStream serviceAccount = new FileInputStream(file);

	final FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl("https://test-10e72.firebaseio.com").build();

	if (FirebaseApp.getApps().isEmpty()) { //<--- check with this line
	    FirebaseApp.initializeApp(options);
	}
	model.addAttribute("userLoginDto", UserLoginDto.builder().build());
	return "/index";
    }



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("userLoginDto") UserLoginDto userLoginDto,final Model model) {
	final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
	final DatabaseReference databaseReference = firebaseDatabase.getReference("adminUsers");

	this.existsUser(databaseReference, userLoginDto.getUser());
	final List<User> users = this.getAllUsers();
	model.addAttribute("users",users);

	while (callBackReturned == null) {

	}

	if (callBackReturned) {
	    return new ModelAndView("/home","updateUserDto",new UpdateUserDto());
	}

	return new ModelAndView("/home2");
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(Model model) {
	final List<User> users = this.getAllUsers();
	model.addAttribute("users",users);

	while (callBackReturned == null) {

	}

	if (callBackReturned) {
	    return new ModelAndView("/home","updateUserDto",new UpdateUserDto());
	}

	return new ModelAndView("/home2");
    }

    @PostMapping(value ="/deleteUser")
    public ModelAndView deleteUser(@ModelAttribute("updateUserDto") UpdateUserDto updateUserDto,final Model model){
	model.asMap().clear();
        final String userID = updateUserDto.getUserId();
	userReturned= null;
        this.removeUserFromDatabase(userID);

        while (userReturned == null){

	}
	callBackReturned = null;
	final List<User> users = this.getAllUsers();
	while(callBackReturned == null) {

	}

	model.addAttribute("users",users);
	return new ModelAndView("/home","updateUserDto",new UpdateUserDto());

    }

    @PostMapping(value ="/banUser")
    public ModelAndView banUser(@ModelAttribute("updateUserDto") UpdateUserDto updateUserDto, Model model){
        model.asMap().clear();
	final String userID = updateUserDto.getUserId();
	userReturned =null;
	this.banUserOnDatabase(userID);

	while (userReturned == null){

	}
	callBackReturned = null;
	final List<User> users = this.getAllUsers();
	while(callBackReturned == null){

	}
	model.addAttribute("users",users);
	return new ModelAndView("/home","updateUserDto",new UpdateUserDto());

    }


    private void removeUserFromDatabase(final String userId){
	this.firebaseThread = new Thread(() -> {
	final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
	final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
	final Query query = databaseReference.orderByChild("id").equalTo(userId);

	query.addListenerForSingleValueEvent(new ValueEventListener() {
	    @Override
	    public void onDataChange(DataSnapshot dataSnapshot) {
		for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
		    appleSnapshot.getRef().removeValue((databaseError, databaseReference1) -> HomeController.userReturned = Boolean.TRUE);
		}
	    }

	    @Override
	    public void onCancelled(DatabaseError databaseError) {
	    }
	});

	});
	this.firebaseThread.start();
    }

    private void banUserOnDatabase(final String userId){
	this.firebaseThread = new Thread(() -> {
	    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
	    final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
	    final Query query = databaseReference.orderByChild("id").equalTo(userId);
	    final DataSnapshot[] currentDataSnapshot = { null };

	    query.addListenerForSingleValueEvent(new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
		    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
			currentDataSnapshot[0] =appleSnapshot;

		    }
		    final Map<String,Object> atributesToUpdate = new ConcurrentHashMap<>();
		    if(Boolean.TRUE.equals(currentDataSnapshot[0].getValue(User.class).getBanned())) {
			atributesToUpdate.put("banned", false);
		    }else{
			atributesToUpdate.put("banned", true);
		    }
		    currentDataSnapshot[0].getRef().updateChildren(atributesToUpdate,new DatabaseReference.CompletionListener() {
			@Override public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
			    HomeController.userReturned = Boolean.TRUE;
			}
		    });
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {
		}
	    });




	});
	this.firebaseThread.start();
    }

    private void existsUser(final DatabaseReference databaseReference, final String user) {
	this.firebaseThread = new Thread(() -> {
	    final List<UserLoginDto> result = new ArrayList();
	    final Query query = databaseReference.orderByChild("user").equalTo(user);
	    query.addListenerForSingleValueEvent(new ValueEventListener() {
		public void onDataChange(DataSnapshot dataSnapshot) {
		    for (DataSnapshot data : dataSnapshot.getChildren()) {
			result.add(data.getValue(UserLoginDto.class));
		    }
		    HomeController.callBackReturned = !(CollectionUtils.isEmpty(result));
		}

		public void onCancelled(DatabaseError databaseError) {

		}
	    });

	});
	this.firebaseThread.start();
    }

    private List<User> getAllUsers() {
	final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
	final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
	final List<User> result = new ArrayList();

	databaseReference.addValueEventListener(new ValueEventListener() {
	    public void onDataChange(DataSnapshot dataSnapshot) {
		for (DataSnapshot data : dataSnapshot.getChildren()) {
		    result.add(data.getValue(User.class));
		}
		HomeController.callBackReturned = !(CollectionUtils.isEmpty(result));
	    }

	    public void onCancelled(DatabaseError databaseError) {

	    }
	});

	return result;

    }

}
