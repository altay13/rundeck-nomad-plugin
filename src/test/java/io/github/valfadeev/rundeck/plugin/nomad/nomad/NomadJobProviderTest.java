package io.github.valfadeev.rundeck.plugin.nomad.nomad;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.hashicorp.nomad.apimodel.Job;
import org.junit.Test;
import utils.TestConfigurationMapBuilder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class NomadJobProviderTest {
    
    @Test
    public void shouldGenerateJobConfig() throws Exception {
        final Map<String, Object> config = TestConfigurationMapBuilder.builder()
                .addItem(NomadConfigOptions.NOMAD_URL, "http://localhost:4646")
                .addItem(NomadConfigOptions.NOMAD_DATACENTER, "")
                .addItem(NomadConfigOptions.NOMAD_REGION, "")
                .addItem(NomadConfigOptions.NOMAD_GROUP_COUNT, "3")
                .addItem(NomadConfigOptions.NOMAD_JOB_TYPE, "service")
                .addItem(NomadConfigOptions.NOMAD_MAX_FAIL_PCT, "0")
                .addItem(NomadConfigOptions.NOMAD_ENV_VARS, "FOO=BAR")
                .addItem(NomadConfigOptions.NOMAD_TASK_CPU, "50")
                .addItem(NomadConfigOptions.NOMAD_TASK_MEMORY, "512")
                .addItem(NomadConfigOptions.NOMAD_TASK_IOPS, "100")
                .addItem(NomadConfigOptions.NOMAD_NETWORK_BANDWIDTH, "10")
                .addItem(NomadConfigOptions.NOMAD_DYNAMIC_PORTS, "http,https")
                .addItem(NomadConfigOptions.NOMAD_RESERVED_PORTS, "amqp=5672\ndb=6379")
                .addItem(NomadConfigOptions.NOMAD_MAX_PARALLEL, "1")
                .addItem(NomadConfigOptions.NOMAD_HEALTH_CHECK, "")
                .addItem(NomadConfigOptions.NOMAD_MIN_HEALTHY_TIME, "")
                .addItem(NomadConfigOptions.NOMAD_HEALTHY_DEADLINE, "")
                .addItem(NomadConfigOptions.NOMAD_AUTO_REVERT, "")
                .addItem(NomadConfigOptions.NOMAD_CANARY, "")
                .addItem(NomadConfigOptions.NOMAD_STAGGER, "")
                .getConfig();

        final Map<String, Object> agentConfig = new HashMap<>();
        agentConfig.put("Datacenter", "dc1");
        agentConfig.put("Region", "global");

        final Map<String, Object> taskConfig = new HashMap<>();


        final Job job = NomadJobProvider.getJob(
                config,
                agentConfig,
                taskConfig,
                "docker",
                "testId",
                "testName",
                "rundeck");

        assertThat(job.getDatacenters(), is(Arrays.asList(new String[]{"dc1"})));
        assertThat(job.getId(), is("testId"));
        assertThat(job.getName(), is("testName"));
        assertThat(job.getTaskGroups().get(0).getName(), is("rundeck"));
        assertThat(job.getRegion(), is("global"));
        assertThat(job.getType(), is("service"));
        assertThat(job.getUpdate().getMaxParallel(), is(1));
    }

}