package com.process.clash.application.auth.electron.port.out;

import java.util.List;

public interface ElectronAuthConfigPort {

	List<String> getAllowedRedirectUris();

	String getAuthWebUrl();

	String getSignupWebUrl();
}
