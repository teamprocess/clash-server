package com.process.clash.application.auth.electron.port.out;

import java.util.List;

public interface ElectronAuthConfigPort {

	List<String> getAllowedRedirectUris();

	String getDefaultRedirectUri();

	String getDevRedirectUri();

	String getAuthWebUrl();

	String getSignupWebUrl();
}
