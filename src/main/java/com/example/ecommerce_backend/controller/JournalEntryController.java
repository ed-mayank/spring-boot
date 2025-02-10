package com.example.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ecommerce_backend.entity.JournalEntry;
import com.example.ecommerce_backend.entity.User;
import com.example.ecommerce_backend.service.JournalEntryService;
import com.example.ecommerce_backend.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<JournalEntry> getAll(){
        return journalEntryService.getAll();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllJournalEntryOfUser(@PathVariable String username){
        User user = userService.findByUsername(username);
        List<JournalEntry> journals = user.getJournal_entries();
        if(journals != null && !journals.isEmpty()){
            return new ResponseEntity<>(journals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String username){
        try{
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable ObjectId myId){
        try {
            Optional<JournalEntry> journalEntry = journalEntryService.getJournalById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{username}/{myId}")
    public ResponseEntity<JournalEntry> updateEntry(
            @PathVariable ObjectId myId, 
            @RequestBody JournalEntry newEntry, 
            @PathVariable String username
        ){
        JournalEntry oldEntry = journalEntryService.getJournalById(myId).orElse(null);
        if(oldEntry != null){
            if(newEntry.getTitle() != null && newEntry.getTitle() != oldEntry.getTitle()){
                oldEntry.setTitle(newEntry.getTitle());
            }
            if(newEntry.getContent() != null && newEntry.getContent() != oldEntry.getContent()){
                oldEntry.setContent(newEntry.getContent());
            }
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{username}/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id, @PathVariable String username){
        journalEntryService.deleteJournal(id, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
