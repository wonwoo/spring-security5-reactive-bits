package sample.user;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.SecureRandom;

/**
 * @author Rob Winch
 * @since 5.0
 */
@Controller
@RequestMapping(path = "/signup")
public class SignupController {
	private SecureRandom random = new SecureRandom();

	private final UserRepository users;

	public SignupController(UserRepository users) {
		this.users = users;
	}

	@GetMapping
	public Mono<String> signupForm(@ModelAttribute User user) {
		return Mono.just("signup/form");
	}

	@PostMapping
	public Mono<String> signup(@Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return signupForm(user);
		}
		return Mono.just(user)
				.doOnNext(u -> u.setId(this.random.nextLong()))
				.flatMap(this.users::save)
				.then(Mono.just("redirect:/"));
	}
}