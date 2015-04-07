/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
package com.exxeta.iss.sonar.esql.parser;

import org.sonar.sslr.internal.text.LocatedText;
import org.sonar.sslr.internal.text.PlainText;
import org.sonar.sslr.text.Text;
import org.sonar.sslr.text.TextCharSequence;
import org.sonar.sslr.text.TextLocation;

public class EsqlLocatedText extends PlainText {

	private LocatedText innerText;

	public EsqlLocatedText(LocatedText arg0) {
		super(arg0.toString().toUpperCase().toCharArray());
		innerText = arg0;
	}

	public TextCharSequence sequence() {
		TextCharSequence result = innerText.sequence();
		return result;
	}

	@Override
	public TextLocation getLocation(int index) {
		return innerText.getLocation(index);
	}
	
	

}
