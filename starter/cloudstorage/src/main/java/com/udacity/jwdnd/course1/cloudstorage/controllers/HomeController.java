package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("home")
public class HomeController {
    private final UserService userService;
    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;

    @GetMapping()
    public String homeView(Model model) {
        int userId = userService.getCurrentUserId();

        model.addAttribute("notes", noteService.getAllByUser(userId));
        model.addAttribute("files", fileService.getAllByUser(userId));
        model.addAttribute("credentials", credentialService.getAllByUser(userId));

        return "home";
    }

    @PostMapping("/note")
    public String postNote(@ModelAttribute("noteModel") Note note, Model model) {
        int userId = userService.getCurrentUserId();
        note.setUserId(userId);

        noteService.upsert(note);

        return "redirect:/home";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer id) {
        boolean deleted = noteService.deleteById(id);

        return "redirect:/home";
    }

    @PostMapping("/file")
    public String postFile(@RequestParam("fileUpload") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            return "redirect:/home?error=Empty File";
        }

        if (fileService.checkExistFile(file.getOriginalFilename())) {
            return "redirect:/home?error=Duplicate Filename";
        }

        int userId = userService.getCurrentUserId();
        try {
            fileService.add(file, userId);
        } catch (IOException e) {
            return "redirect:/home?error=Error happened when upload file";
        }

        return "redirect:/home";
    }

    @GetMapping("/file/delete/{id}")
    public String deleteFile(@PathVariable("id") Integer id) {
        boolean deleted = fileService.deleteById(id);

        if (deleted) {
            return "redirect:/home";
        } else {
            return "redirect:/home?error=File Not Exist";
        }
    }

    @GetMapping("/file/view/{id}")
    public void viewFile(@PathVariable("id") Integer fileId, HttpServletResponse response) throws IOException {
        // Retrieve the BLOB data and content type from the database based on the fileId
        File file = fileService.findById(fileId);

        // Convert the BLOB data to a byte array
        byte[] fileBytes = file.getFileData();

        // Set the content type based on the retrieved content type
        response.setContentType(file.getContentType());

        // Set the content disposition to "inline" to display the file in the browser
        response.setHeader("Content-Disposition", "inline");

        // Write the byte array to the response output stream
        response.getOutputStream().write(fileBytes);
    }

    @PostMapping("/credential")
    public String postCredential(@ModelAttribute("credentialModel") Credential credential, Model model) {
        int userId = userService.getCurrentUserId();
        credential.setUserId(userId);

        credentialService.upsert(credential);

        return "redirect:/home";
    }

    @GetMapping("/credential/delete/{id}")
    public String deleteCredential(@PathVariable("id") Integer id) {
        boolean deleted = credentialService.deleteById(id);

        if (deleted) {
            return "redirect:/home";
        } else {
            return "redirect:/home?error=File Not Exist";
        }
    }
}
