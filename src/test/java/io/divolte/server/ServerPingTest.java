/*
 * Copyright 2018 GoDataDriven B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.divolte.server;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import io.divolte.server.ServerTestUtils.TestServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

@ParametersAreNonnullByDefault
public class ServerPingTest {

    @Nullable
    private TestServer testServer;

    @Before
    public void setup() {
        testServer = new TestServer();
    }

    @Test
    public void shouldRespondToPingWithPong() throws IOException {
        Preconditions.checkState(null != testServer);
        final URL url = new URL(String.format("http://%s:%d/ping", testServer.host, testServer.port));
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestMethod("GET");
            assertEquals(200, conn.getResponseCode());
            assertEquals("text/plain; charset=utf-8", conn.getContentType());
            final String body = new String(ByteStreams.toByteArray(conn.getInputStream()), StandardCharsets.UTF_8);
            assertEquals("pong", body);
        } finally {
            conn.disconnect();
        }
    }

    @After
    public void tearDown() {
        if (null != testServer) {
            testServer.shutdown();
            testServer = null;
        }
    }
}
