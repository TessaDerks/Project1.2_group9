package physics;


public class VerletSolver implements PhysicsEngine{
    private double t = 0;
    private double m = 45.93;
    private double g = 9.81;
    private double mu;
    private double dt;
    private double v_max;
    private Vector2d p;
    private Vector2d last_p;
    private Vector2d v;
    private Function2d h;
    private Vector2d G;
    private Vector2d H;
    private Vector2d F;

    public VerletSolver(Vector2d _p){
        p = _p;
    }

    @Override
    public void actTimestep() {
        recalculate();
        Vector2d temp_p;
        double temp_p_x;
        double temp_p_y;
        double nv_x;
        double nv_y;

        // First time step.
        if(t<dt){
            // New position.
            temp_p_x = p.get_x() + v.get_x() * dt + ((F.get_x() * Math.pow(dt,2)) / (2 * m));
            temp_p_y = p.get_y() + v.get_y() * dt + ((F.get_y() * Math.pow(dt,2)) / (2 * m));
            temp_p = new Vector2d(temp_p_x,temp_p_y);

            System.out.println(v.get_x() + "v" + v.get_y());
            System.out.println(temp_p_x + "p" + temp_p_y);

            // New velocity.
            nv_x = (temp_p_x - p.get_x()) / dt;
            nv_y = (temp_p_y - p.get_y()) / dt;
            v = new Vector2d(nv_x,nv_y);
        }
        // All other time steps.
        else{
            // New position.
            temp_p_x = 2 * p.get_x() - last_p.get_x() + ((F.get_x() * Math.pow(dt,2)) / m);
            temp_p_y = 2 * p.get_y() - last_p.get_y() + ((F.get_y() * Math.pow(dt,2)) / m);
            temp_p = new Vector2d(temp_p_x,temp_p_y);
            
            // New velocity.
            nv_x = (temp_p_x - last_p.get_x()) / (2 * dt);
            nv_y = (temp_p_y - last_p.get_y()) / (2 * dt);
            v = new Vector2d(nv_x,nv_y);
        }
        
        // Recalibrate.
        last_p = p;
        p = temp_p;
        t = Tools.advRound(t+dt,6);
       // sendPosition();
    }

    @Override
    public void sendPosition(){
       // What.ent.setPosition((float) p.get_x(),(float) (h.evaluate(new Vector2d(p.get_x(),p.get_y()))+0.15),(float) p.get_y());
    }

    //<editor-fold desc="Calculators">
    @Override
    public void recalculate(){
        G = calcG();
        H = calcH();
        F = calcF();
    }

    @Override
    public Vector2d calcG(){
        Vector2d der = h.gradient(p);
        return new Vector2d(-m*g*der.get_x(),-m*g*der.get_y());
    }

    @Override
    public Vector2d calcH(){
        double xH = (-mu * m * g * v.get_x()) /* Math.sqrt(Math.pow(v.get_x(),2) + Math.pow(v.get_y(),2))*/;
        double yH = (-mu * m * g * v.get_y()) /* Math.sqrt(Math.pow(v.get_x(),2) + Math.pow(v.get_y(),2))*/;
        return new Vector2d(xH,yH);
    }

    @Override
    public Vector2d calcF(){
        return new Vector2d(G.get_x()+H.get_x(),G.get_y()+H.get_y());
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    @Override
    public void setMu(double _mu) {
        mu = _mu;
    }

    @Override
    public void set_m(double _m) {
        m = _m / 1000;
    }

    @Override
    public void set_g(double _g) {
        g = _g;
    }

    @Override
    public void set_v(Vector2d _v) {
        if(Math.sqrt(Math.pow(_v.get_x(),2)+Math.pow(_v.get_y(),2))>v_max)
        {
            v = new Vector2d(0,0);
        }
        else {
            v = _v;
        }
    }


    @Override
    public void set_h(Function2d _h) {
        h = _h;
    }

    @Override
    public void set_v_max(double _v_max) {
        v_max = _v_max;
    }

    @Override
    public void set_step_size(double _dt) {
        dt = _dt;
    }

    @Override
    public void set_t(double _t) {
        t = _t;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    @Override
    public Function2d get_h() {
        return h;
    }

    @Override
    public Vector2d get_p() {
        return p;
    }

    @Override
    public Vector2d get_v() {
        return v;
    }

    public double get_t(){
        return t;
    }
    //</editor-fold>
}
