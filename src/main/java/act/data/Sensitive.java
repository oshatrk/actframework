package act.data;

/*-
 * #%L
 * ACT Framework
 * %%
 * Copyright (C) 2014 - 2017 ActFramework
 * %%
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
 * #L%
 */

import act.asm.ClassVisitor;
import act.util.AppByteCodeEnhancer;
import org.osgl.util.S;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a **String** typed field is sensitive.
 * DB plugin should sense any field with this annotation so that
 * sensitive data be encrypted when stored into database and
 * decrypted after retrieving from database
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sensitive {

    class Enhancer extends AppByteCodeEnhancer<Enhancer> {
        public Enhancer() {
            super(S.F.startsWith("act.").negate());
        }

        public Enhancer(ClassVisitor cv) {
            super(S.F.startsWith("act.").negate(), cv);
        }

        @Override
        protected Class<Enhancer> subClass() {
            return Enhancer.class;
        }

    }
}
