/*
 *  Copyright 2014 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beaker.jvm.object;

import com.twosigma.beaker.BeakerCodeCell;
import com.twosigma.beaker.BeakerProgressUpdate;
import com.twosigma.beaker.chart.xychart.CombinedPlot;
import com.twosigma.beaker.chart.xychart.Plot;
import com.twosigma.beaker.chart.xychart.XYChart;
import com.twosigma.beaker.chart.xychart.plotitem.XYGraphics;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;

public class SerializeUtils {

  public static void writeObject(Object obj, JsonGenerator jgen)
      throws IOException, JsonProcessingException {

    try {
      if (obj == null) {
        jgen.writeObject("null");
      } else if (obj instanceof TableDisplay) {
        jgen.writeObject(obj);
      } else if (obj instanceof OutputContainer) {
        jgen.writeObject(obj);
      } else if (obj instanceof XYChart) {
        jgen.writeObject((XYChart) obj);
      } else if (obj instanceof XYGraphics) {
        jgen.writeObject(new Plot().add((XYGraphics) obj));
      } else if (obj instanceof CombinedPlot) {
        jgen.writeObject((CombinedPlot) obj);
      } else if (obj instanceof BeakerProgressUpdate) {
        jgen.writeObject(obj);
      } else if (obj instanceof BeakerCodeCell) {
        jgen.writeObject(obj);
      } else if (obj instanceof Collection<?>) {
        Collection<?> c = (Collection<?>) obj;
        jgen.writeStartArray();
        for(Object o : c)
          writeObject(o, jgen);
        jgen.writeEndArray();
      } else if (obj instanceof Map<?,?>) {
        Map<?,?> m = (Map<?,?>) obj;

        Set<?> kset = m.keySet();
        if (kset.size()==0 || !(kset.iterator().next() instanceof String))
          jgen.writeObject(obj.toString());
        else {
          jgen.writeStartObject();
          for (Object k : kset) {
            jgen.writeFieldName(k.toString());
            writeObject(m.get(k),jgen);
          }
          jgen.writeEndObject();
        }
      } else {
        jgen.writeObject(obj.toString());
      }
    } catch (IOException e) {
      System.err.println(e);
      jgen.writeObject("null");
    }

  }
}
