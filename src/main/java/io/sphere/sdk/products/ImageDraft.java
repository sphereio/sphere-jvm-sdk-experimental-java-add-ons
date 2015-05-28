package io.sphere.sdk.products;

import io.sphere.sdk.models.Base;

import java.io.File;
import java.util.Optional;

public class ImageDraft extends Base {
    private final Optional<String> filename;
    private final File img;
    private final String contentType;

    private ImageDraft(final String contentType, final File img, final Optional<String> filename) {
        this.contentType = contentType;
        this.filename = filename;
        this.img = img;
    }

    public String getContentType() {
        return contentType;
    }

    public Optional<String> getFilename() {
        return filename;
    }

    public File getImg() {
        return img;
    }

    public static ImageDraft ofGif(final File img) {
        return of("image/gif", img);
    }

    public static ImageDraft ofPng(final File img) {
        return of("image/png", img);
    }

    public static ImageDraft ofJpeg(final File img) {
        return of("image/jpeg", img);
    }

    private static ImageDraft of(final String contentType, final File img) {
        return new ImageDraft(contentType, img, Optional.of(img.getName()));
    }

    public ImageDraft withFileName(final Optional<String> fileName) {
        return new ImageDraft(getContentType(), getImg(), fileName);
    }

    public ImageDraft withFileName(final String fileName) {
        return withFileName(Optional.of(fileName));
    }
}
