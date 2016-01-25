package code.breakmc.legacy.utils;

import lombok.NonNull;

import java.beans.ConstructorProperties;

public class TeamInfo {

    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private String suffix;

    @ConstructorProperties({"name", "prefix", "suffix"})
    public TeamInfo(@NonNull String name, @NonNull String prefix, @NonNull String suffix) {
        if (name == null) {
            throw new NullPointerException("name");
        } else if (prefix == null) {
            throw new NullPointerException("prefix");
        } else if (suffix == null) {
            throw new NullPointerException("suffix");
        } else {
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
        }
    }

    public int hashCode() {
        byte result = 1;
        String $name = this.getName();
        int result1 = result * 59 + ($name == null ? 0 : $name.hashCode());
        String $prefix = this.getPrefix();
        result1 = result1 * 59 + ($prefix == null ? 0 : $prefix.hashCode());
        String $suffix = this.getSuffix();
        result1 = result1 * 59 + ($suffix == null ? 0 : $suffix.hashCode());
        return result1;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TeamInfo;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TeamInfo)) {
            return false;
        } else {
            TeamInfo other = (TeamInfo) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label43:
                {
                    String this$name = this.getName();
                    String other$name = other.getName();
                    if (this$name == null) {
                        if (other$name == null) {
                            break label43;
                        }
                    } else if (this$name.equals(other$name)) {
                        break label43;
                    }

                    return false;
                }

                String this$prefix = this.getPrefix();
                String other$prefix = other.getPrefix();
                if (this$prefix == null) {
                    if (other$prefix != null) {
                        return false;
                    }
                } else if (!this$prefix.equals(other$prefix)) {
                    return false;
                }

                String this$suffix = this.getSuffix();
                String other$suffix = other.getSuffix();
                return this$suffix == null ? other$suffix == null : this$suffix.equals(other$suffix);
            }
        }
    }

    public void setName(@NonNull String name) {
        if (name == null) {
            throw new NullPointerException("name");
        } else {
            this.name = name;
        }
    }

    public void setPrefix(@NonNull String prefix) {
        if (prefix == null) {
            throw new NullPointerException("prefix");
        } else {
            this.prefix = prefix;
        }
    }

    public void setSuffix(@NonNull String suffix) {
        if (suffix == null) {
            throw new NullPointerException("suffix");
        } else {
            this.suffix = suffix;
        }
    }

    public String toString() {
        return "TeamInfo(name=" + this.getName() + ", prefix=" + this.getPrefix() + ", suffix=" + this.getSuffix() + ")";
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getPrefix() {
        return this.prefix;
    }

    @NonNull
    public String getSuffix() {
        return this.suffix;
    }
}