/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asual.lesscss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * @author Rostislav Hristov
 */
public class StyleResource extends Resource {

    public StyleResource(String path, Object resource, 
    		String charset, boolean compress) {
		super(path, resource, charset, compress);
	}

    public byte[] getContent(String path) throws Exception {
        if (content == null || (content != null && lastModified < getLastModified())) {
        	content = resource instanceof URL ? ResourceUtils.readUrl((URL) resource) : ResourceUtils.readFile((File) resource);
        	lastModified = getLastModified();
        	if (compress) {
        		compress();
        	}
        }
        return content;
    }
    
	protected void compress() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Reader in = new InputStreamReader(
                new ByteArrayInputStream(
                        (new String(content))
                            .replaceFirst("/\\*", "/*!")
                            .getBytes(charset)), 
                            charset);
        Writer out = new OutputStreamWriter(baos, charset);
        CssCompressor compressor = new CssCompressor(in);
        in.close();
        compressor.compress(out, -1);
        out.flush();
        content = baos.toByteArray();
        out.close();
    }
    
}