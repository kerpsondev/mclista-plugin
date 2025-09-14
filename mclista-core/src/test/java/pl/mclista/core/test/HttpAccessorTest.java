package pl.mclista.core.test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import pl.mclista.api.common.client.ApiFailtureCause;
import pl.mclista.api.common.client.RewardApiResponse;
import pl.mclista.api.common.developer.DeveloperAction;
import pl.mclista.core.client.RewardApiClientImpl;
import pl.mclista.core.configuration.section.PluginConfiguration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpAccessorTest {

  private static PluginConfiguration pluginConfiguration;
  private static RewardApiClientImpl rewardApiClient;

  @BeforeAll
  static void initialize() {
    pluginConfiguration = Mockito.mock(PluginConfiguration.class);
    UUID testServerId = UUID.fromString("f16264d8-e593-477e-b9c2-a4145ab14a78");
    Mockito.when(pluginConfiguration.getServerId()).thenReturn(testServerId);

    rewardApiClient = new RewardApiClientImpl(pluginConfiguration);
  }

  @Test
  @Order(1)
  void successTest() throws ExecutionException, InterruptedException {
    DeveloperAction<RewardApiResponse> developerAction = rewardApiClient.sendRequest("kerpson").get();
    RewardApiResponse response = developerAction.getResult();

    Assertions.assertTrue(response.isSuccess(), "Response is not success!");
  }

  @Test
  @Order(2)
  void invalidNickname() throws ExecutionException, InterruptedException {
    DeveloperAction<RewardApiResponse> developerAction = rewardApiClient.sendRequest("fuckingInvalidNicknameThatNotMatch").get();
    RewardApiResponse response = developerAction.getResult();

    Assertions.assertSame(ApiFailtureCause.INVALID_NICKNAME, response.getCause().get(), "Response is success!");
  }

  @Test
  @Order(3)
  void invalidNeverUpvoted() throws ExecutionException, InterruptedException {
    DeveloperAction<RewardApiResponse> developerAction = rewardApiClient.sendRequest("neverUpvoted").get();
    RewardApiResponse response = developerAction.getResult();

    Assertions.assertSame(ApiFailtureCause.NEVER_UPVOTED, response.getCause().get(), "Response is success!");
  }

  @Test
  @Order(4)
  void invalidServerNotFound() throws ExecutionException, InterruptedException {
    Mockito.when(pluginConfiguration.getServerId()).thenReturn(UUID.randomUUID());

    DeveloperAction<RewardApiResponse> developerAction = rewardApiClient.sendRequest("kerpson").get();
    RewardApiResponse response = developerAction.getResult();

    Assertions.assertSame(ApiFailtureCause.SERVER_NOT_FOUND, response.getCause().get(), "Response is success!");
  }
}
