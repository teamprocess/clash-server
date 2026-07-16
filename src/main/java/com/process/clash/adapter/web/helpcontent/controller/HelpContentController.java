package com.process.clash.adapter.web.helpcontent.controller;

import com.process.clash.adapter.web.helpcontent.docs.controller.HelpContentControllerDocument;
import com.process.clash.application.helpcontent.data.GetHelpContentData;
import com.process.clash.application.helpcontent.port.in.GetHelpContentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/help-contents")
@RequiredArgsConstructor
public class HelpContentController implements HelpContentControllerDocument {

    private final GetHelpContentUseCase getHelpContentUseCase;

    @Override
    @GetMapping("/{key}")
    public ResponseEntity<String> getHelpContent(
            @PathVariable String key,
            WebRequest request
    ) {
        GetHelpContentData.Result result = getHelpContentUseCase.execute(key);
        String etag = "\"" + result.version() + "\"";

        if (request.checkNotModified(etag)) {
            return null;
        }

        return ResponseEntity.ok()
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .cacheControl(CacheControl.noCache())
                .eTag(etag)
                .body(result.content());
    }
}
