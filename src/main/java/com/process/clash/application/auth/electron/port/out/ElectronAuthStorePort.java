package com.process.clash.application.auth.electron.port.out;

public interface ElectronAuthStorePort {

	void saveState(String state);

	boolean validateState(String state);

	boolean consumeState(String state);

	void saveOneTimeCode(String code, String state, Long userId);

	OneTimeCodePayload consumeOneTimeCode(String code);

	void saveSignupSession(String state, String redirectUri);

	String consumeSignupSession(String state);

	record OneTimeCodePayload(String state, Long userId) {}
}
