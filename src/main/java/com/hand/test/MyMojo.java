package com.hand.test;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.codehaus.plexus.util.FileUtils.getFiles;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
@Mojo(name = "testHello")
public class MyMojo
        extends AbstractMojo {
    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    @Parameter(
            required = true,
            property = "javaOutputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/test/java"
    )
    private File outputDirectory;

    /**
     * @parameter expression="${project.basedir}"
     * @required
     * @readonly
     */
    @Parameter(
            required = true,
            property = "basedir",
            defaultValue = "${project.basedir}/srm/main/java"
    )
    private File basedir;

    /**
     * The current Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    public void execute()
            throws MojoExecutionException {

        try {
            final List<File> listBaseFile =
                    getFiles(basedir, "**/*", null);
            for (File file : listBaseFile) {
                System.out.println(file.getName() + ":" + file.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        File touch = new File(f, "touch.txt");

        FileWriter w = null;
        try {
            w = new FileWriter(touch);

            w.write("touch.txt");
            w.write(project.getFile().toString());
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + touch, e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
