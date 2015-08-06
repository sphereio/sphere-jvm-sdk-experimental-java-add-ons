package io.sphere.sdk.products.commands;

import io.sphere.sdk.client.WithSphereClient;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.*;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeDraft;
import io.sphere.sdk.producttypes.commands.ProductTypeCreateCommand;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductImageUploadTest extends WithSphereClient {

    private static final String SLUG = ProductImageUploadTest.class.getSimpleName();
    private static final ProductVariantDraft MASTER_VARIANT = ProductVariantDraftBuilder.of().build();

    @Test
    public void execution() throws Exception {
        final ProductProjection product = getProduct();
        final String randomPartInImageName = RandomStringUtils.randomAlphanumeric(32);
        final String imageName = randomPartInImageName + ".gif";
        assertThat(product.getMasterVariant().getImages()).isEmpty();
        final ProductImageUploadCommand uploadCommand = ProductImageUploadCommand.of(ImageDraft.ofGif(getImage()).withFileName(imageName), product.getId(), 1);
        final Product updatedProduct = client().execute(uploadCommand).toCompletableFuture().join();

        final Image image = updatedProduct.getMasterData().getStaged().getMasterVariant().getImages().get(0);
        assertThat(image.getDimensions().getHeight()).isEqualTo(102);
        assertThat(image.getDimensions().getWidth()).isEqualTo(460);
        assertThat(image.getUrl()).contains("https:");
    }

    private ProductProjection getProduct() {
        return createProduct().toCompletableFuture().join();
    }

    private CompletionStage<ProductProjection> createProduct() {
        return productType().thenCompose(productType -> {
            final ProductDraft productDraft =
                    ProductDraftBuilder.of(productType, LocalizedString.ofEnglishLocale("test"),
                            LocalizedString.ofEnglishLocale(RandomStringUtils.randomAlphanumeric(32)), MASTER_VARIANT)
                            .build();
            return client().execute(ProductCreateCommand.of(productDraft)).thenApply(product -> product.toProjection(ProductProjectionType.STAGED));
        });
    }

    private CompletionStage<ProductType> productType() {
        return getOrCreate(ProductTypeQuery.of().byName(SLUG), this::createProductType);
    }

    private CompletionStage<ProductType> createProductType() {
        final ProductTypeDraft productTypeDraft = ProductTypeDraft.of(SLUG, "", asList());
        return client().execute(ProductTypeCreateCommand.of(productTypeDraft));
    }

    private File getImage() {
        return new File(".", "src/it/resources/ct_logo_farbe.gif");
    }

}
