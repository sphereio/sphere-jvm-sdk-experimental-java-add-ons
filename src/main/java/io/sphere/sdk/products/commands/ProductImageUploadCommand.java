package io.sphere.sdk.products.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.commands.CommandImpl;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.models.Identifiable;
import io.sphere.sdk.products.*;

import java.io.File;
import java.util.Optional;

import static java.lang.String.format;

public class ProductImageUploadCommand extends CommandImpl<Product> {
    private final String productId;
    private final int variantId;
    private final Optional<String> filename;
    private final File img;
    private final String contentType;

    private ProductImageUploadCommand(final String productId, final int variantId,
                                      final Optional<String> filename,
                                      final String contentType, final File img) {
        this.productId = productId;
        this.variantId = variantId;
        this.filename = filename;
        this.img = img;
        this.contentType = contentType;
    }

    @Override
    protected TypeReference<Product> typeReference() {
        return Product.typeReference();
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        final String path = format("/products/%s/images?variant=%d%s", productId, variantId, filename.map(s -> "&filename="+s).orElse(""));
        return HttpRequestIntent.of(HttpMethod.POST, path, img, contentType);
    }

    public static ProductImageUploadCommand of(final ImageDraft draft,  final VariantIdentifier variantIdentifier) {
        return of(draft, variantIdentifier.getProductId(), variantIdentifier.getVariantId());
    }

    public static ProductImageUploadCommand of(final ImageDraft draft,  final Identifiable<Product> productIdentifiable, final int variantId) {
        return of(draft, productIdentifiable.getId(), variantId);
    }

    public static ProductImageUploadCommand of(final ImageDraft draft,  final String productId, final int variantId) {
        return new ProductImageUploadCommand(productId, variantId, draft.getFilename(), draft.getContentType(), draft.getImg());
    }

    @Deprecated
    public static ProductImageUploadCommand of(final String productId, final int variantId,
                                                           final Optional<String> filename,
                                                           final String contentType, final File img) {
        return new ProductImageUploadCommand(productId, variantId, filename, contentType, img);
    }

    @Deprecated
    public static ProductImageUploadCommand of(final Identifiable<Product> product, final int variantId,
                                                           final Optional<String> filename,
                                                           final String contentType, final File img) {
        return of(product.getId(), variantId, filename, contentType, img);
    }
}
