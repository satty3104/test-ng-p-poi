package test.tmp;

import com.google.inject.Binder;
import com.google.inject.Module;

public class Mod implements Module {

  @Override
  public void configure(Binder b) {
    b.bind(String.class).toInstance("" + System.currentTimeMillis());
  }
  
}

