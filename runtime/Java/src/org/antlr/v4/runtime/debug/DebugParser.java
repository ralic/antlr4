/*
 [The "BSD license"]
 Copyright (c) 2005-2009 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
     derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.antlr.v4.runtime.debug;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;

public class DebugParser extends Parser {
	/** Who to notify when events in the parser occur. */
	protected DebugEventListener dbg = null;

	/** Create a normal parser except wrap the token stream in a debug
	 *  proxy that fires consume events.
	 */
	public DebugParser(TokenStream input, DebugEventListener dbg) {
		super(input instanceof DebugTokenStream?input:new DebugTokenStream(input,dbg));
		setDebugListener(dbg);
	}

	public DebugParser(TokenStream input) {
		super(input instanceof DebugTokenStream ? input : new DebugTokenStream(input, null));
	}

	/** Provide a new debug event listener for this parser.  Notify the
	 *  input stream too that it should send events to this listener.
	 */
	public void setDebugListener(DebugEventListener dbg) {
		if ( input instanceof DebugTokenStream ) {
			((DebugTokenStream)input).setDebugListener(dbg);
		}
		this.dbg = dbg;
	}

	public DebugEventListener getDebugListener() {
		return dbg;
	}

	public void reportError(IOException e) {
		System.err.println(e);
		e.printStackTrace(System.err);
	}

	public void beginResync() {
		dbg.beginResync();
	}

	public void endResync() {
		dbg.endResync();
	}

	public void reportError(RecognitionException e) {
		super.reportError(e);
		dbg.recognitionException(e);
	}
}
