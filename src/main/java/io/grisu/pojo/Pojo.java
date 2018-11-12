package io.grisu.pojo;

import java.lang.reflect.Type;
import java.util.Set;

public interface Pojo extends Gettable, Puttable {

   Set<String> keySet();

   Type __getTypeOf(String property);

   boolean __hasChanged(String property);

   void __resetHashes();

}
