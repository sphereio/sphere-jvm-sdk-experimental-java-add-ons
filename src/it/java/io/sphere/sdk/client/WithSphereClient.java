package io.sphere.sdk.client;

import io.sphere.sdk.queries.Query;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public abstract class WithSphereClient {
    private static SphereClient client;

    @BeforeClass
    public static void setUp() throws Exception {
        final SphereClientConfig config = SphereClientConfig.ofEnvironmentVariables("SPHERE_JVM_SDK_EXPERIMENTAL_JAVA_ADDONS");
        client = SphereClientFactory.of().createClient(config);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.close();
    }

    protected static SphereClient client() {
        return client;
    }

    protected static <T> CompletionStage<T> getOrCreate(final Query<T> query, final Supplier<CompletionStage<T>> supplier) {
        return getOrCreate(client(), query, supplier);
    }

    private static <T> CompletionStage<T> getOrCreate(final SphereClient client, final Query<T> query, final Supplier<CompletionStage<T>> supplier) {
        return client.execute(query).thenCompose(queryResult -> queryResult.head().map(WithSphereClient::completed).orElseGet(supplier));
    }

    private static <T> CompletionStage<T> completed(final T t) {
        return CompletableFuture.completedFuture(t);
    }
}
