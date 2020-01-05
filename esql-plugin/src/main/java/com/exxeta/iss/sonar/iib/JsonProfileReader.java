/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
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
package com.exxeta.iss.sonar.iib;

import com.google.common.io.Resources;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class JsonProfileReader {

    private JsonProfileReader() {
    }

    public static Set<String> ruleKeys(String pathToFile) {
        URL profileUrl = JsonProfileReader.class.getResource(pathToFile);
        try {
            Gson gson = new Gson();
            return gson.fromJson(Resources.toString(profileUrl, StandardCharsets.UTF_8), Profile.class).ruleKeys;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read: " + profileUrl, e);
        }
    }

    private static class Profile {
        Set<String> ruleKeys;
    }


}
