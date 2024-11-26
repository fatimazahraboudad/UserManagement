package com.project.UserService.exceptions;

public class InvitationAlreadyAcceptedException extends RuntimeException{
    public InvitationAlreadyAcceptedException() {
        super(ExceptionMessages.INVITATION_ALREADY_ACCEPTED.getMessage());
    }
}
