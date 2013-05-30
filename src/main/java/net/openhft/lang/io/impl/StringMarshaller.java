/*
 * Copyright ${YEAR} Peter Lawrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.lang.io.impl;

import net.openhft.lang.io.Bytes;
import net.openhft.lang.io.BytesMarshaller;
import net.openhft.lang.io.StopCharTester;

/**
 * @author peter.lawrey
 */
public class StringMarshaller implements BytesMarshaller<String> {
    private final int size1;
    private String[] interner;

    public StringMarshaller(int size) {
        int size2 = 128;
        while (size2 < size && size2 < (1 << 20)) size2 <<= 1;
        this.size1 = size2 - 1;
    }

    @Override
    public Class<String> classMarshaled() {
        return String.class;
    }

    @Override
    public void write(Bytes bytes, String s) {
        bytes.writeUTF(s);
    }

    @Override
    public void append(Bytes bytes, String s) {
        bytes.append(s);
    }

    private final StringBuilder reader = new StringBuilder();

    @Override
    public String read(Bytes bytes) {
        if (bytes.readUTF(reader))
            return builderToString();
        return null;
    }

    @Override
    public String parse(Bytes bytes, StopCharTester tester) {
        reader.setLength(0);
        bytes.parseUTF(reader, tester);
        return builderToString();
    }

    private String builderToString() {
        int idx = hashFor(reader);
        if (interner == null)
            interner = new String[size1 + 1];

        String s2 = interner[idx];
        if (s2 != null && s2.length() == reader.length())
            NOT_FOUND:{
                for (int i = 0, len = s2.length(); i < len; i++) {
                    if (s2.charAt(i) != reader.charAt(i))
                        break NOT_FOUND;
                }
                return s2;
            }
        return interner[idx] = reader.toString();
    }

    private int hashFor(CharSequence cs) {
        long h = 0;

        for (int i = 0, length = cs.length(); i < length; i++)
            h = 57 * h + cs.charAt(i);

        h ^= (h >>> 43) ^ (h >>> 21);
        h ^= (h >>> 15) ^ (h >>> 7);
        return (int) (h & size1);
    }
}