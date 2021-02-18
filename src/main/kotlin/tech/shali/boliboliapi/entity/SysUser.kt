package tech.shali.boliboliapi.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class SysUser(
    @Column(nullable = false, unique = true) private var username: String,
    @Column(nullable = false) @JsonIgnore private var password: String,
    @Column(nullable = false) @Enumerated(EnumType.STRING) var role: SysRole = SysRole.NORMAL,
) : Data(), UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val list = ArrayList<GrantedAuthority>()
        list.add(SimpleGrantedAuthority(role.name))
        return list
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled(): Boolean = true

}