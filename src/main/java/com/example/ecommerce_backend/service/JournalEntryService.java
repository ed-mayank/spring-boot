package com.example.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ecommerce_backend.entity.JournalEntry;
import com.example.ecommerce_backend.entity.User;
import com.example.ecommerce_backend.repository.JournalEntryRepository;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry journalEntry, String username){
        User user = userService.findByUsername(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournal_entries().add(saved);
        userService.saveEntry(user);
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getJournalById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public Optional<JournalEntry> deleteJournal(ObjectId id, String username){
        User user = userService.findByUsername(username);
        for(JournalEntry journal : user.getJournal_entries()){
            if(journal.getId().equals(id)){
                user.getJournal_entries().remove(journal);
            }
        }
        userService.saveEntry(user);
        Optional<JournalEntry> journal = getJournalById(id);
        journalEntryRepository.deleteById(id);
        return journal;
    }
}
