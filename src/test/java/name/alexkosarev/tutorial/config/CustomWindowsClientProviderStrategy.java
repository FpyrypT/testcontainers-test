package name.alexkosarev.tutorial.config;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.dockerclient.InvalidConfigurationException;
import org.testcontainers.dockerclient.WindowsClientProviderStrategy;
import org.testcontainers.utility.CommandLine;
import org.testcontainers.utility.DockerMachineClient;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.testcontainers.shaded.com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class CustomWindowsClientProviderStrategy extends WindowsClientProviderStrategy {

    @Override
    public void test() throws InvalidConfigurationException {

        try {
//            boolean installed = DockerMachineClient.instance().isInstalled();
//            checkArgument(installed, "docker-machine executable was not found on PATH (" + Arrays.toString(CommandLine.getSystemPath()) + ")");
//
//            Optional<String> machineNameOptional = DockerMachineClient.instance().getDefaultMachine();
//            checkArgument(machineNameOptional.isPresent(), "docker-machine is installed but no default machine could be found");
//            String machineName = machineNameOptional.get();
//
//            log.info("Found docker-machine, and will use machine named {}", machineName);
//
//            DockerMachineClient.instance().ensureMachineRunning(machineName);
//
//            String dockerDaemonIpAddress = DockerMachineClient.instance().getDockerDaemonIpAddress(machineName);

//            log.info("Docker daemon IP address for docker machine {} is {}", machineName, dockerDaemonIpAddress);

            config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("tcp://" + "192.168.0.130" + ":2376")
                    .withDockerTlsVerify(true)
                    .withDockerCertPath(Paths.get(System.getProperty("user.home") + "/.docker/machine/certs/").toString())
                    .build();
            client = getClientForConfig(config);
        } catch (Exception e) {
            throw new InvalidConfigurationException(e.getMessage());
        }

        // If the docker-machine VM has started, the docker daemon may still not be ready. Retry pinging until it works.
        final int timeout = Integer.parseInt(System.getProperty("testcontainers.dockermachineprovider.timeout", "30"));
        ping(client, timeout);
    }

    @Override
    protected int getPriority() {
        return 90;
    }
}
