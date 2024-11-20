package com.project.SubscriptionService.controllers;

import com.project.SubscriptionService.dtos.SubscriptionDto;
import com.project.SubscriptionService.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions/request")
    public ResponseEntity<SubscriptionDto> add(@RequestBody SubscriptionDto subscriptionDto) {
        return new ResponseEntity<>(subscriptionService.add(subscriptionDto), HttpStatus.CREATED);
    }

    @GetMapping("/admin/subscriptions")
    public ResponseEntity<List<SubscriptionDto>> getAll() {
        return new ResponseEntity<>(subscriptionService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/admin/subscriptions/{idSubscription}")
    public ResponseEntity<SubscriptionDto> getById(@PathVariable String idSubscription) {
        return new ResponseEntity<>(subscriptionService.getById(idSubscription), HttpStatus.OK);
    }

    @PutMapping("/admin/subscriptions/update")
    public ResponseEntity<SubscriptionDto> update(@RequestBody SubscriptionDto subscriptionDto) {
        return new ResponseEntity<>(subscriptionService.update(subscriptionDto), HttpStatus.OK);
    }

    @DeleteMapping("/admin/subscriptions/delete/{idSubscription}")
    public ResponseEntity<String> update(@PathVariable String idSubscription) {
        return new ResponseEntity<>(subscriptionService.delete(idSubscription), HttpStatus.OK);
    }

    @PutMapping("/admin/subscriptions/{idSubscription}/approve")
    public ResponseEntity<SubscriptionDto> approveRequest(@PathVariable String idSubscription) {
        return new ResponseEntity<>(subscriptionService.approveRequest(idSubscription), HttpStatus.OK);
    }

    @PutMapping("/admin/subscriptions/{idSubscription}/reject")
    public ResponseEntity<SubscriptionDto> rejectRequest(@PathVariable String idSubscription) {
        return new ResponseEntity<>(subscriptionService.rejectRequest(idSubscription), HttpStatus.OK);
    }





}
