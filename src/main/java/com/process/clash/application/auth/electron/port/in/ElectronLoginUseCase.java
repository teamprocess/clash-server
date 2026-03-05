package com.process.clash.application.auth.electron.port.in;

public interface ElectronLoginUseCase {

	String execute(String username, String password, String state, String redirectUri);
}
