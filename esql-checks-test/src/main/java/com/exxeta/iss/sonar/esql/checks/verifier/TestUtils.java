package com.exxeta.iss.sonar.esql.checks.verifier;

import static com.exxeta.iss.sonar.esql.compat.CompatibilityHelper.wrap;

import java.io.IOException;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.MapSettings;
import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.google.common.base.Throwables;
import com.sonar.sslr.api.typed.ActionParser;

public class TestUtils {
	protected static final ActionParser<Tree> p = newParser();

	private TestUtils() {
	}

	private static ActionParser<Tree> newParser() {
		return EsqlParserBuilder.createParser();
	}

	public static EsqlVisitorContext createContext(InputFile file) {
		return createContext(file, p);
	}

	public static EsqlVisitorContext createParallelContext(InputFile file) {
		return createContext(file, newParser());
	}

	private static EsqlVisitorContext createContext(InputFile file, ActionParser<Tree> parser) {
		try {
			ProgramTree programTree = (ProgramTree) parser.parse(file.contents());
			return new EsqlVisitorContext(programTree, wrap(file), settings());
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private static Settings settings() {
		Settings settings = new MapSettings();

		return settings;
	}
}
