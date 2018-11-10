/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.msgflow.check.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorContext;
import com.exxeta.iss.sonar.msgflow.parser.Msgflow;
import com.exxeta.iss.sonar.msgflow.parser.MsgflowParser;
import com.exxeta.iss.sonar.msgflow.parser.MsgflowParserBuilder;
import com.google.common.base.Throwables;

public class TestUtils {
	protected static final MsgflowParser p = newParser();

	public static MsgflowVisitorContext createContext(final InputFile file) {
		return createContext(file, p);
	}

	private static MsgflowVisitorContext createContext(final InputFile file, final MsgflowParser parser) {
		try {
			final Msgflow msgflow = parser.parse(file.contents());
			return new MsgflowVisitorContext(msgflow, file);
		} catch (final IOException e) {
			throw Throwables.propagate(e);
		}
	}

	public static MsgflowVisitorContext createParallelContext(final InputFile file) {
		return createContext(file, newParser());
	}

	public static DefaultInputFile createTestInputFile(final File baseDir, final String relativePath) {
		return createTestInputFile(baseDir.getAbsolutePath(), relativePath);
	}

	public static DefaultInputFile createTestInputFile(final File baseDir, final String relativePath,
			final Charset charset) {
		return new TestInputFileBuilder(baseDir.getAbsolutePath(), relativePath)
				.setModuleBaseDir(Paths.get(baseDir.getAbsolutePath())).setLanguage("esql").setCharset(charset).build();
	}

	public static DefaultInputFile createTestInputFile(final String relativePath) {
		return createTestInputFile("", relativePath);
	}

	public static DefaultInputFile createTestInputFile(final String baseDir, final String relativePath) {
		return new TestInputFileBuilder("module1", relativePath).setModuleBaseDir(Paths.get(baseDir))
				.setLanguage("esql").setCharset(StandardCharsets.UTF_8).setType(InputFile.Type.MAIN).build();
	}

	private static MsgflowParser newParser() {
		return MsgflowParserBuilder.createParser();
	}

	private TestUtils() {
	}

}
