package org.firstinspires.ftc.teamcode.util;

import com.bylazar.configurables.annotations.Configurable;


public final class Constants{



   //TODO: MAKE CONFIGURABLE

    @Configurable
    public static final class TurretConstants{
       public static int TUNING_POLLEN_ANGLE = 0;
       public static int TUNING_NECTAR_ANGLE = 0;

       public static double TUNING_NECTAR_POWER = 0;
       public static double TUNING_POLLEN_POWER = 0;




       public static double POLLEN_TURRET_FORWARD_OFFSET = -0.0; // TODO: measure
       public static double POLLEN_TURRET_LATERAL_OFFSET = 0.0;
       public static double NECTAR_TURRET_FORWARD_OFFSET = -0.0;
       public static double NECTAR_TURRET_LATERAL_OFFSET = -0.0;



       public static double T_POLLEN_KP = 0.01;
       public static double T_POLLEN_KI = 0;
       public static double T_POLLEN_KD = 0.001;
       public static double T_POLLEN_KF = 0.0;



       public static double T_NECTAR_KP = 0.01;
       public static double T_NECTAR_KI = 0;
       public static double T_NECTAR_KD = 0.001;
       public static double T_NECTAR_KF = 0.0;



       public static final double NECTAR_GEAR_RATIO = 1.2;
       public static final double POLLEN_GEAR_RATIO = 1.364;
       public static double NECTAR_ZERO_DEG = 0;
       public static double POLLEN_ZERO_DEG = 0;
       public static double NECTAR_ANGLE_SIGN = 1;
       public static double POLLEN_ANGLE_SIGN = 1;

       public static double TUNING_POLLEN_POSITION = 0.5;
       public static double TUNING_NECTAR_POSITION = 0.5;
       public static double NECTAR_CENTER_POS = 0.5;
       public static double POLLEN_CENTER_POS = 0.5;
       public static double SERVO_RANGE_DEG = 255.0;

   }

   @Configurable

   public static final class ShooterConstants{
       public static int POLLEN_TUNING_RPM = 3000;
      public static int NECTAR_TUNING_RPM = 0;


      public static double S_POLLEN_KP = 0.0025;
      public static double S_POLLEN_KI = 0.0;
      public static double S_POLLEN_KD = 0.0;
      public static double S_POLLEN_KF = 0.00016;



      public static double S_NECTAR_KP = 0.0;
      public static double S_NECTAR_KI = 0.0;
      public static double S_NECTAR_KD = 0.0;
      public static double S_NECTAR_KF = 0.0;



   }

   @Configurable
   public static final class IntakeConstants{

       public static double INTAKE_TUNING_POWER = 0;
       public static double TRANSFER_TUNING_POWER = 0;


   }



}