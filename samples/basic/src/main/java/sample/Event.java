
package sample;

import com.formkiq.graalvm.annotations.Reflectable;

@Reflectable
public class Event {

	@Reflectable
	private String type;

	public void setType(String type) {
		this.type = type;
	}

    public String getType() {
        return this.type;
    }
}
