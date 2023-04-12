public class test {
    public static void main(String[] args) {
        A refAobjA = new A();
        A refAobjB = new B(); // implicit casting
        B refBobjB = (B)refAobjB; // explicit casting (reference from A->B; super->sub)
        A refAobjB_2 = refBobjB; // implicit casting (reference from B->A; sub->super)
        // System.out.println(refAobjB_2.y); // error; y does not exist in refAobjB 
                                          // *check from reference type, not object*
        System.out.println(((B)refAobjB_2).y); // variable is casted. reference changed
        System.out.println(refBobjB.y); // intuitively allowed
        System.out.println(((B)refAobjA).y); // runtime error: class A <object> cannot be cast to class B
                                             // the red underline only indicate error at
                                             // compile time (before object is instantiated at runtime)
    }
}

class A {
    int x = 1;
}

class B extends A {
    int y = 2;
}

class C extends B {
    int z = 2;
}
